package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.common.config.AppProperties;
import com.ats.resumescanner.scan.JobDescription;
import com.ats.resumescanner.scan.SuggestionSeverity;
import com.ats.resumescanner.scan.model.KeywordCoverage;
import com.ats.resumescanner.scan.model.ScanResult;
import com.ats.resumescanner.scan.model.Suggestion;
import com.ats.resumescanner.scan.skills.SkillDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class JobDescriptionScoringStrategy implements ScoringStrategy {

    private final AppProperties appProperties;
    private final SkillDictionary skillDictionary;

    private static final Pattern YEARS_PATTERN = Pattern.compile("(\\d{1,2})\\+?\\s*(years|yrs)", Pattern.CASE_INSENSITIVE);
    private static final List<String> SECTION_KEYWORDS = List.of("summary", "skills", "experience", "education", "projects", "certifications");
    private static final double DEFAULT_SKILLS_SCORE = 50.0;

    @Override
    public ScanResult score(ScoringContext context) {
        String resumeText = context.getResume().getExtractedText();
        String jdText = Optional.ofNullable(context.getJobDescription()).map(JobDescription::getText).orElse("");

        double keywordScore = TextSimilarity.keywordScore(resumeText, jdText) * 100;

        List<String> jdSkills = TextSimilarity.matchedTokens(jdText, skillDictionary.allSkills());
        List<String> matchedSkills = TextSimilarity.matchedTokens(resumeText, jdSkills);
        List<String> missingSkills = new ArrayList<>(jdSkills);
        missingSkills.removeAll(matchedSkills);
        double skillsScore = jdSkills.isEmpty() ? DEFAULT_SKILLS_SCORE : Math.min(100.0, ((double) matchedSkills.size() / jdSkills.size()) * 100);

        double experienceScore = computeExperienceScore(resumeText, jdText);
        double formattingScore = computeFormattingScore(resumeText);

        List<String> sectionsDetected = detectSections(resumeText);

        KeywordCoverage coverage = KeywordCoverage.builder()
                .matchedKeywords(matchedSkills)
                .missingKeywords(missingSkills.stream().limit(20).toList())
                .build();

        List<Suggestion> suggestions = buildSuggestions(coverage, formattingScore, sectionsDetected, experienceScore);

        double overallScore = weightedAverage(keywordScore, skillsScore, experienceScore, formattingScore);

        return ScanResult.builder()
                .overallScore(round(overallScore))
                .keywordMatchScore(round(keywordScore))
                .skillsMatchScore(round(skillsScore))
                .experienceMatchScore(round(experienceScore))
                .formattingScore(round(formattingScore))
                .sectionsDetected(sectionsDetected)
                .keywordCoverage(coverage)
                .suggestions(suggestions)
                .extras(Map.of("strategy", "JD_MATCH"))
                .build();
    }

    private double weightedAverage(double keywordScore, double skillsScore, double experienceScore, double formattingScore) {
        AppProperties.Scoring weights = appProperties.getScoring();
        double total = weights.getKeywordWeight() + weights.getSkillsWeight() + weights.getExperienceWeight() + weights.getFormattingWeight();
        return (keywordScore * weights.getKeywordWeight()
                + skillsScore * weights.getSkillsWeight()
                + experienceScore * weights.getExperienceWeight()
                + formattingScore * weights.getFormattingWeight()) / total;
    }

    private double computeExperienceScore(String resumeText, String jdText) {
        int resumeYears = extractYears(resumeText);
        int jdYears = extractYears(jdText);
        if (jdYears == 0) {
            return resumeYears > 0 ? 80 : 50;
        }
        int diff = Math.abs(resumeYears - jdYears);
        if (diff == 0) return 100;
        if (diff <= 1) return 85;
        if (diff <= 2) return 75;
        return 55;
    }

    private int extractYears(String text) {
        Matcher matcher = YEARS_PATTERN.matcher(text);
        int max = 0;
        while (matcher.find()) {
            try {
                int years = Integer.parseInt(matcher.group(1));
                max = Math.max(max, years);
            } catch (NumberFormatException ignored) {
            }
        }
        return max;
    }

    private double computeFormattingScore(String text) {
        double score = 100;
        String lower = text.toLowerCase(Locale.ENGLISH);
        if (lower.contains("table")) score -= 10;
        if (lower.contains("column")) score -= 10;
        if (lower.contains("header") || lower.contains("footer")) score -= 5;
        if (lower.contains("image")) score -= 10;
        if (text.length() > 5000) score -= 5;
        return Math.max(score, 20);
    }

    private List<String> detectSections(String text) {
        String lower = text.toLowerCase(Locale.ENGLISH);
        List<String> found = new ArrayList<>();
        for (String section : SECTION_KEYWORDS) {
            if (lower.contains(section)) {
                found.add(capitalize(section));
            }
        }
        return found;
    }

    private List<Suggestion> buildSuggestions(KeywordCoverage coverage, double formattingScore, List<String> sections, double experienceScore) {
        List<Suggestion> suggestions = new ArrayList<>();
        if (!coverage.getMissingKeywords().isEmpty()) {
            suggestions.add(Suggestion.builder()
                    .severity(SuggestionSeverity.WARN)
                    .message("Add missing keywords: " + String.join(", ", coverage.getMissingKeywords().subList(0, Math.min(10, coverage.getMissingKeywords().size()))))
                    .build());
        }
        if (formattingScore < 80) {
            suggestions.add(Suggestion.builder()
                    .severity(SuggestionSeverity.WARN)
                    .message("Improve formatting for ATS (avoid tables/columns/headers)")
                    .build());
        }
        if (!sections.contains("Summary")) {
            suggestions.add(Suggestion.builder()
                    .severity(SuggestionSeverity.INFO)
                    .message("Add a concise Summary section")
                    .build());
        }
        if (experienceScore < 70) {
            suggestions.add(Suggestion.builder()
                    .severity(SuggestionSeverity.CRITICAL)
                    .message("Align years of experience with job requirement")
                    .build());
        }
        return suggestions;
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
}

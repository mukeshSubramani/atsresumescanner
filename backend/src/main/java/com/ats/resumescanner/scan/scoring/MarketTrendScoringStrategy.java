package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.scan.SuggestionSeverity;
import com.ats.resumescanner.scan.model.KeywordCoverage;
import com.ats.resumescanner.scan.model.ScanResult;
import com.ats.resumescanner.scan.model.Suggestion;
import com.ats.resumescanner.scan.skills.SkillDictionary;
import com.ats.resumescanner.common.util.TextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MarketTrendScoringStrategy implements ScoringStrategy {

    private final SkillDictionary skillDictionary;

    @Override
    public ScanResult score(ScoringContext context) {
        String resumeText = context.getResume().getExtractedText();
        String aggregatedMarketJd = (String) context.getParameters().getOrDefault("marketText", "");

        double keywordScore = TextSimilarity.keywordScore(resumeText, aggregatedMarketJd) * 100;
        List<String> matchedSkills = TextSimilarity.matchedTokens(resumeText, skillDictionary.allSkills());
        List<String> marketSkills = topKeywords(aggregatedMarketJd);
        List<String> missingKeywords = new ArrayList<>(marketSkills);
        missingKeywords.removeAll(matchedSkills);

        double skillsScore = Math.min(100.0, ((double) matchedSkills.size() / (matchedSkills.size() + missingKeywords.size() + 1e-6)) * 100);
        double experienceScore = 75; // heuristic placeholder
        double formattingScore = 90; // reuse formatting intuition

        KeywordCoverage coverage = KeywordCoverage.builder()
                .matchedKeywords(matchedSkills)
                .missingKeywords(missingKeywords.stream().limit(20).toList())
                .build();

        List<Suggestion> suggestions = List.of(
                Suggestion.builder().severity(SuggestionSeverity.INFO).message("Focus on trending skills: " + String.join(", ", missingKeywords.stream().limit(8).toList())).build(),
                Suggestion.builder().severity(SuggestionSeverity.WARN).message("Highlight recent projects with these skills").build()
        );

        double overallScore = (keywordScore * 0.4) + (skillsScore * 0.35) + (experienceScore * 0.15) + (formattingScore * 0.1);
        overallScore /= 1.0;

        return ScanResult.builder()
                .overallScore(round(overallScore))
                .keywordMatchScore(round(keywordScore))
                .skillsMatchScore(round(skillsScore))
                .experienceMatchScore(round(experienceScore))
                .formattingScore(round(formattingScore))
                .sectionsDetected(List.of())
                .keywordCoverage(coverage)
                .suggestions(suggestions)
                .extras(Map.of("strategy", "MARKET_TREND", "marketKeywords", marketSkills))
                .build();
    }

    private List<String> topKeywords(String text) {
        Map<String, Integer> tf = TextSimilarity.termFrequency(TextUtils.normalize(text, true));
        return tf.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(20)
                .map(Map.Entry::getKey)
                .toList();
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

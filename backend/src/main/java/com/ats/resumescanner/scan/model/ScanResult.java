package com.ats.resumescanner.scan.model;

import com.ats.resumescanner.scan.SuggestionSeverity;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ScanResult {
    private double overallScore;
    private double keywordMatchScore;
    private double skillsMatchScore;
    private double experienceMatchScore;
    private double formattingScore;
    private List<String> sectionsDetected;
    private KeywordCoverage keywordCoverage;
    private List<Suggestion> suggestions;
    private Map<String, Object> extras;
}

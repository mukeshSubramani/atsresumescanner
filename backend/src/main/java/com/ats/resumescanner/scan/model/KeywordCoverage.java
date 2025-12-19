package com.ats.resumescanner.scan.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class KeywordCoverage {
    private List<String> matchedKeywords;
    private List<String> missingKeywords;
}

package com.ats.resumescanner.scan.model;

import com.ats.resumescanner.scan.SuggestionSeverity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Suggestion {
    private String message;
    private SuggestionSeverity severity;
}

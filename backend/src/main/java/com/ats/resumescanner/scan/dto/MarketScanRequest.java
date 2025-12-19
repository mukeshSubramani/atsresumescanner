package com.ats.resumescanner.scan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MarketScanRequest {
    @NotNull
    private Long resumeId;
    @NotBlank
    private String roleKey;
    private String location = "UK";
}

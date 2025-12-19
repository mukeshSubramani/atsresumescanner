package com.ats.resumescanner.scan.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class JdScanRequest {
    @NotNull
    private Long resumeId;
    @NotBlank
    private String jdText;
    private String title;
    private String company;
    private String location;
}

package com.ats.resumescanner.scan.dto;

import com.ats.resumescanner.scan.ScanType;
import com.ats.resumescanner.scan.model.ScanResult;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ScanResponse {
    private Long id;
    private ScanType scanType;
    private Instant createdAt;
    private ScanResult result;
}

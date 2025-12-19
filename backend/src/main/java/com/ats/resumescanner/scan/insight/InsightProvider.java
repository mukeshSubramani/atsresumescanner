package com.ats.resumescanner.scan.insight;

import com.ats.resumescanner.scan.model.ScanResult;

public interface InsightProvider {
    String getName();
    ScanResult enrich(ScanResult result);
}

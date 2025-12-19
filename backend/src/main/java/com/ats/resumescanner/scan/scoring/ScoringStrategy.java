package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.scan.model.ScanResult;

public interface ScoringStrategy {
    ScanResult score(ScoringContext context);
}

package com.ats.resumescanner.scan.insight;

import com.ats.resumescanner.scan.model.ScanResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GrammarInsightProvider implements InsightProvider {
    @Override
    public String getName() {
        return "GrammarInsightProvider";
    }

    @Override
    public ScanResult enrich(ScanResult result) {
        // Stub for future grammar analysis
        log.debug("Grammar insight provider stub invoked");
        return result;
    }
}

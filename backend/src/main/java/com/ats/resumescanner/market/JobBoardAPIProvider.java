package com.ats.resumescanner.market;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JobBoardAPIProvider implements MarketDatasetProvider {
    @Override
    public List<MarketRoleTemplate> listTemplates() {
        return List.of();
    }

    @Override
    public List<String> loadJobDescriptions(String roleKey, String location) {
        // Stub for future external job board integration
        return List.of();
    }

    @Override
    public String providerName() {
        return "JobBoardAPIProvider";
    }
}

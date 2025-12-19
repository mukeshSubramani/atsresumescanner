package com.ats.resumescanner.market;

import java.util.List;

public interface MarketDatasetProvider {
    List<MarketRoleTemplate> listTemplates();
    List<String> loadJobDescriptions(String roleKey, String location);
    String providerName();
}

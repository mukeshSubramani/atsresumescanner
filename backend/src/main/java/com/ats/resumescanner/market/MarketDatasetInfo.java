package com.ats.resumescanner.market;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MarketDatasetInfo {
    private String provider;
    private int datasetCount;
    private List<String> roles;
    private String locationDefault;
}

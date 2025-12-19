package com.ats.resumescanner.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "features")
public class FeatureFlagConfig {
    private Map<String, Boolean> flags = new HashMap<>();

    public boolean isEnabled(String key) {
        return flags.getOrDefault(key, false);
    }
}

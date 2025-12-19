package com.ats.resumescanner.market;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LocalDatasetProvider implements MarketDatasetProvider {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<MarketRoleTemplate> templates = List.of();
    private Map<String, List<String>> datasets = Map.of();

    @PostConstruct
    public void load() {
        try {
            templates = objectMapper.readValue(
                    new ClassPathResource("data/market/roles.json").getInputStream(),
                    new TypeReference<>() {});
            datasets = objectMapper.readValue(
                    new ClassPathResource("data/market/datasets.json").getInputStream(),
                    new TypeReference<>() {});
        } catch (IOException e) {
            log.error("Failed to load market datasets: {}", e.getMessage());
            templates = Collections.emptyList();
            datasets = Collections.emptyMap();
        }
    }

    @Override
    public List<MarketRoleTemplate> listTemplates() {
        return templates;
    }

    @Override
    public List<String> loadJobDescriptions(String roleKey, String location) {
        String key = roleKey + "::" + (location == null ? "UK" : location.toUpperCase());
        return datasets.getOrDefault(key, List.of());
    }

    @Override
    public String providerName() {
        return "LocalDatasetProvider";
    }
}

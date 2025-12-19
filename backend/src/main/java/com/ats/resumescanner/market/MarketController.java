package com.ats.resumescanner.market;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/market")
@RequiredArgsConstructor
public class MarketController {

    private final MarketDatasetProvider datasetProvider;

    @GetMapping("/roles")
    public ResponseEntity<List<MarketRoleTemplate>> roles() {
        return ResponseEntity.ok(datasetProvider.listTemplates());
    }

    @GetMapping("/datasets/info")
    public ResponseEntity<MarketDatasetInfo> info() {
        List<MarketRoleTemplate> templates = datasetProvider.listTemplates();
        return ResponseEntity.ok(MarketDatasetInfo.builder()
                .provider(datasetProvider.providerName())
                .datasetCount(templates.size())
                .roles(templates.stream().map(MarketRoleTemplate::getKey).toList())
                .locationDefault("UK")
                .build());
    }
}

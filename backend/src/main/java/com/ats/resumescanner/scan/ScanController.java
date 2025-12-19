package com.ats.resumescanner.scan;

import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.scan.dto.JdScanRequest;
import com.ats.resumescanner.scan.dto.MarketScanRequest;
import com.ats.resumescanner.scan.dto.ScanResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/scans")
@RequiredArgsConstructor
public class ScanController {

    private final ScanService scanService;

    @PostMapping("/jd-match")
    public ResponseEntity<ScanResponse> jdMatch(@AuthenticationPrincipal User user,
                                                @Valid @RequestBody JdScanRequest request) {
        return ResponseEntity.ok(scanService.jdScan(user, request));
    }

    @PostMapping("/market-trend")
    public ResponseEntity<ScanResponse> marketTrend(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody MarketScanRequest request) {
        return ResponseEntity.ok(scanService.marketScan(user, request));
    }

    @GetMapping
    public ResponseEntity<List<ScanResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(scanService.listScans(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScanResponse> get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(scanService.getScan(user, id));
    }
}

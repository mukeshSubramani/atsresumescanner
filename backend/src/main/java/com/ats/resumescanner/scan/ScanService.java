package com.ats.resumescanner.scan;

import com.ats.resumescanner.audit.AuditLogService;
import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.common.exception.ResourceNotFoundException;
import com.ats.resumescanner.resume.Resume;
import com.ats.resumescanner.resume.ResumeService;
import com.ats.resumescanner.scan.dto.JdScanRequest;
import com.ats.resumescanner.scan.dto.MarketScanRequest;
import com.ats.resumescanner.scan.dto.ScanResponse;
import com.ats.resumescanner.scan.model.ScanResult;
import com.ats.resumescanner.scan.scoring.JobDescriptionScoringStrategy;
import com.ats.resumescanner.scan.scoring.MarketTrendScoringStrategy;
import com.ats.resumescanner.scan.scoring.ScoringContext;
import com.ats.resumescanner.market.MarketDatasetProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScanService {

    private final ResumeService resumeService;
    private final JobDescriptionRepository jobDescriptionRepository;
    private final ScanRepository scanRepository;
    private final JobDescriptionScoringStrategy jdScoringStrategy;
    private final MarketTrendScoringStrategy marketTrendScoringStrategy;
    private final MarketDatasetProvider datasetProvider;
    private final ObjectMapper objectMapper;
    private final AuditLogService auditLogService;

    @Transactional
    public ScanResponse jdScan(User user, JdScanRequest request) {
        Resume resume = resumeService.getResume(user, request.getResumeId());
        JobDescription jd = JobDescription.builder()
                .user(user)
                .title(request.getTitle())
                .company(request.getCompany())
                .location(request.getLocation())
                .text(request.getJdText())
                .build();
        jobDescriptionRepository.save(jd);

        ScanResult result = jdScoringStrategy.score(ScoringContext.builder()
                .resume(resume)
                .jobDescription(jd)
                .build());

        Scan scan = saveScan(user, resume, jd, ScanType.JD_MATCH, result);
        auditLogService.log(user, "JD_SCAN", toJson(Map.of("scanId", scan.getId())));
        return toResponse(scan, result);
    }

    @Transactional
    public ScanResponse marketScan(User user, MarketScanRequest request) {
        Resume resume = resumeService.getResume(user, request.getResumeId());
        List<String> jds = datasetProvider.loadJobDescriptions(request.getRoleKey(), request.getLocation());
        String aggregated = jds.stream().collect(Collectors.joining("\n\n"));
        ScanResult result = marketTrendScoringStrategy.score(ScoringContext.builder()
                .resume(resume)
                .parameters(Map.of("marketText", aggregated))
                .build());
        Scan scan = saveScan(user, resume, null, ScanType.MARKET_TREND, result);
        auditLogService.log(user, "MARKET_SCAN", toJson(Map.of("scanId", scan.getId(), "roleKey", request.getRoleKey())));
        return toResponse(scan, result);
    }

    @Transactional(readOnly = true)
    public List<ScanResponse> listScans(User user) {
        return scanRepository.findAllByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ScanResponse getScan(User user, Long id) {
        Scan scan = scanRepository.findById(id)
                .filter(s -> s.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Scan not found"));
        return toResponse(scan);
    }

    private Scan saveScan(User user, Resume resume, JobDescription jd, ScanType type, ScanResult result) {
        try {
            Scan scan = Scan.builder()
                    .user(user)
                    .resume(resume)
                    .jobDescription(jd)
                    .scanType(type)
                    .resultJson(objectMapper.writeValueAsString(result))
                    .build();
            return scanRepository.save(scan);
        } catch (Exception e) {
            throw new RuntimeException("Failed to persist scan", e);
        }
    }

    private ScanResponse toResponse(Scan scan) {
        try {
            ScanResult result = objectMapper.readValue(scan.getResultJson(), ScanResult.class);
            return toResponse(scan, result);
        } catch (Exception e) {
            throw new RuntimeException("Failed to map scan result", e);
        }
    }

    private ScanResponse toResponse(Scan scan, ScanResult result) {
        return ScanResponse.builder()
                .id(scan.getId())
                .scanType(scan.getScanType())
                .createdAt(scan.getCreatedAt())
                .result(result)
                .build();
    }

    private String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return "{}";
        }
    }
}

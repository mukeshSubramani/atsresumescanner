package com.ats.resumescanner.resume.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ResumeResponse {
    private Long id;
    private String name;
    private String fileType;
    private Instant createdAt;
    private Instant updatedAt;
}

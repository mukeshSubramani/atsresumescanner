package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.scan.JobDescription;
import com.ats.resumescanner.resume.Resume;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class ScoringContext {
    private Resume resume;
    private JobDescription jobDescription;
    private Map<String, Object> parameters;
}

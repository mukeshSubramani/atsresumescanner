package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.common.config.AppProperties;
import com.ats.resumescanner.resume.Resume;
import com.ats.resumescanner.scan.JobDescription;
import com.ats.resumescanner.scan.model.ScanResult;
import com.ats.resumescanner.scan.skills.SkillDictionary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JobDescriptionScoringStrategyTest {

    private JobDescriptionScoringStrategy strategy;

    @BeforeEach
    void setup() {
        AppProperties props = new AppProperties();
        SkillDictionary dict = new SkillDictionary();
        dict.load();
        strategy = new JobDescriptionScoringStrategy(props, dict);
    }

    @Test
    void scoresResumeAgainstJd() {
        Resume resume = Resume.builder().extractedText("Java Spring Boot AWS Docker Kubernetes").build();
        JobDescription jd = JobDescription.builder().text("We need Java and Spring Boot with AWS").build();
        ScanResult result = strategy.score(ScoringContext.builder().resume(resume).jobDescription(jd).build());
        assertTrue(result.getOverallScore() > 30);
        assertTrue(result.getKeywordCoverage().getMatchedKeywords().contains("java"));
    }
}

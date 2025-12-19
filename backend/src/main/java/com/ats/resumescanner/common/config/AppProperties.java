package com.ats.resumescanner.common.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Data
@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @NotNull
    private Security security = new Security();

    @NotNull
    private Storage storage = new Storage();

    @NotNull
    private Scoring scoring = new Scoring();

    @Data
    public static class Security {
        @NotBlank
        private String jwtSecret = "change-me";
        private long jwtExpirationSeconds = 3600;
    }

    @Data
    public static class Storage {
        @NotBlank
        private String resumeDir = "./data/resumes";
        private long maxFileSizeBytes = 10 * 1024 * 1024;
    }

    @Data
    public static class Scoring {
        private double keywordWeight = 0.35;
        private double skillsWeight = 0.25;
        private double experienceWeight = 0.2;
        private double formattingWeight = 0.2;
        private Map<String, Double> sectionWeights;
    }
}

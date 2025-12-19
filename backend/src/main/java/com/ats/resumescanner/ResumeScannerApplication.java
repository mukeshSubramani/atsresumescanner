package com.ats.resumescanner;

import com.ats.resumescanner.common.config.AppProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan
@RequiredArgsConstructor
public class ResumeScannerApplication {

    private final AppProperties appProperties;
    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(ResumeScannerApplication.class, args);
    }

    @PostConstruct
    public void validateConfiguration() {
        String jwtSecret = appProperties.getSecurity().getJwtSecret();
        boolean isProd = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        
        if (isProd && "please-change-me-32-bytes-min".equals(jwtSecret)) {
            throw new IllegalStateException(
                "SECURITY ERROR: Default JWT secret detected in production environment. " +
                "Please set a strong JWT_SECRET environment variable (minimum 32 bytes)."
            );
        }
        
        if ("please-change-me-32-bytes-min".equals(jwtSecret)) {
            log.warn("WARNING: Using default JWT secret. This is insecure for production use. " +
                    "Set JWT_SECRET environment variable to a strong secret (minimum 32 bytes).");
        }
    }
}

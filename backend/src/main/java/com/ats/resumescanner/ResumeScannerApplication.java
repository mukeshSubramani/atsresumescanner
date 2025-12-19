package com.ats.resumescanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ResumeScannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResumeScannerApplication.class, args);
    }
}

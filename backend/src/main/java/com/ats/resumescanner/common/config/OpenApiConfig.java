package com.ats.resumescanner.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI resumeScannerOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("ATS Resume Scanner API")
                        .description("API documentation for ATS Resume Scanner")
                        .version("v1"))
                .externalDocs(new ExternalDocumentation()
                        .description("Project repository")
                        .url("https://github.com/mukeshSubramani/atsresumescanner"));
    }
}

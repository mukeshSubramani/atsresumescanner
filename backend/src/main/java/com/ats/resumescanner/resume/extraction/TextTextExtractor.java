package com.ats.resumescanner.resume.extraction;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

@Component
public class TextTextExtractor implements TextExtractor {
    @Override
    public boolean supports(String contentType, String extension) {
        return contentType != null && contentType.startsWith("text/") || "txt".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(InputStream inputStream) throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }
}

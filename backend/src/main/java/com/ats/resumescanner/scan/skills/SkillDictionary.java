package com.ats.resumescanner.scan.skills;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SkillDictionary {
    private List<String> skills = List.of();

    @PostConstruct
    public void load() {
        try (var in = new ClassPathResource("data/skills.json").getInputStream()) {
            String json = new String(in.readAllBytes());
            this.skills = List.copyOf(new com.fasterxml.jackson.databind.ObjectMapper().readValue(json, List.class));
        } catch (IOException e) {
            log.warn("Failed to load skills dictionary: {}", e.getMessage());
            this.skills = Collections.emptyList();
        }
    }

    public List<String> allSkills() {
        return skills;
    }
}

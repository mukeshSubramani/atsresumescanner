package com.ats.resumescanner.scan.scoring;

import com.ats.resumescanner.common.util.TextUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class TextSimilarity {
    private TextSimilarity() {}

    public static double keywordScore(String source, String target) {
        Map<String, Integer> sourceTf = termFrequency(TextUtils.normalize(source, true));
        Map<String, Integer> targetTf = termFrequency(TextUtils.normalize(target, true));
        Set<String> terms = new HashSet<>();
        terms.addAll(sourceTf.keySet());
        terms.addAll(targetTf.keySet());
        if (terms.isEmpty()) return 0;
        double dot = 0;
        double normA = 0;
        double normB = 0;
        for (String term : terms) {
            int a = sourceTf.getOrDefault(term, 0);
            int b = targetTf.getOrDefault(term, 0);
            dot += a * b;
            normA += a * a;
            normB += b * b;
        }
        if (normA == 0 || normB == 0) return 0;
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    public static Map<String, Integer> termFrequency(String text) {
        if (text == null || text.isBlank()) {
            return Map.of();
        }
        Map<String, Integer> tf = new HashMap<>();
        for (String token : text.split(" ")) {
            if (token.isBlank()) continue;
            tf.merge(token.trim(), 1, Integer::sum);
        }
        return tf;
    }

    public static List<String> matchedTokens(String text, Collection<String> keywords) {
        Set<String> normalizedTokens = Arrays.stream(TextUtils.normalize(text, true).split(" "))
                .filter(t -> !t.isBlank())
                .collect(Collectors.toSet());
        return keywords.stream()
                .map(k -> TextUtils.normalize(k, true))
                .filter(normalizedTokens::contains)
                .distinct()
                .toList();
    }
}

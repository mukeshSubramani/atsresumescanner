package com.ats.resumescanner.common.util;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class TextUtils {
    private static final Set<String> STOP_WORDS = Set.of("the", "and", "or", "a", "an", "of", "in", "to", "for", "on", "with", "at", "by", "is", "are", "be");
    private static final Pattern NON_WORD = Pattern.compile("[^a-z0-9\s]");

    private TextUtils() {
    }

    public static String normalize(String input, boolean removeStopWords) {
        if (input == null) {
            return "";
        }
        String lower = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ENGLISH);
        String cleaned = NON_WORD.matcher(lower).replaceAll(" ").replaceAll("\\s+", " ").trim();
        if (!removeStopWords) {
            return cleaned;
        }
        List<String> filtered = Arrays.stream(cleaned.split(" "))
                .filter(word -> !STOP_WORDS.contains(word))
                .toList();
        return filtered.stream().collect(Collectors.joining(" "));
    }
}

package com.ats.resumescanner.resume.extraction;

import com.ats.resumescanner.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextExtractionService {

    private final List<TextExtractor> extractors;

    public String extractText(MultipartFile file) {
        String extension = getExtension(file.getOriginalFilename());
        for (TextExtractor extractor : extractors) {
            if (extractor.supports(file.getContentType(), extension)) {
                try {
                    return extractor.extract(file.getInputStream());
                } catch (IOException e) {
                    throw new BadRequestException("Failed to extract text: " + e.getMessage());
                }
            }
        }
        throw new BadRequestException("Unsupported file type");
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.') + 1);
    }
}

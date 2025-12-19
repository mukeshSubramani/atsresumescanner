package com.ats.resumescanner.resume.extraction;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Component
public class DocxTextExtractor implements TextExtractor {
    @Override
    public boolean supports(String contentType, String extension) {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document".equalsIgnoreCase(contentType)
                || "application/msword".equalsIgnoreCase(contentType)
                || "docx".equalsIgnoreCase(extension)
                || "doc".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(InputStream inputStream) throws IOException {
        try (XWPFDocument doc = new XWPFDocument(inputStream)) {
            return doc.getParagraphs().stream()
                    .map(XWPFParagraph::getText)
                    .collect(Collectors.joining("\n"));
        }
    }
}

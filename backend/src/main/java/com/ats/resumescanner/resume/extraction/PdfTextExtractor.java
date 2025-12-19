package com.ats.resumescanner.resume.extraction;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public class PdfTextExtractor implements TextExtractor {
    @Override
    public boolean supports(String contentType, String extension) {
        return "application/pdf".equalsIgnoreCase(contentType) || "pdf".equalsIgnoreCase(extension);
    }

    @Override
    public String extract(InputStream inputStream) throws IOException {
        try (PDDocument document = PDDocument.load(inputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}

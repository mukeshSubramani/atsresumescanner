package com.ats.resumescanner.resume.extraction;

import java.io.IOException;
import java.io.InputStream;

public interface TextExtractor {
    boolean supports(String contentType, String extension);

    String extract(InputStream inputStream) throws IOException;
}

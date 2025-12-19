package com.ats.resumescanner.resume;

import com.ats.resumescanner.common.config.AppProperties;
import com.ats.resumescanner.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResumeStorageService {

    private final AppProperties appProperties;

    public String store(MultipartFile file) {
        if (file.getSize() > appProperties.getStorage().getMaxFileSizeBytes()) {
            throw new BadRequestException("File too large");
        }
        Path storageDir = Path.of(appProperties.getStorage().getResumeDir());
        try {
            Files.createDirectories(storageDir);
            String filename = UUID.randomUUID() + "-" + sanitize(file.getOriginalFilename());
            Path destination = storageDir.resolve(filename);
            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
            return destination.toAbsolutePath().toString();
        } catch (IOException e) {
            throw new BadRequestException("Failed to store file");
        }
    }

    public void delete(String filePath) {
        if (filePath == null) return;
        try {
            Files.deleteIfExists(Path.of(filePath));
        } catch (IOException ignored) {
        }
    }

    private String sanitize(String originalFilename) {
        if (originalFilename == null) return "file";
        return originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
    }
}

package com.ats.resumescanner.resume;

import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.common.exception.ResourceNotFoundException;
import com.ats.resumescanner.resume.dto.ResumeResponse;
import com.ats.resumescanner.resume.extraction.TextExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final ResumeStorageService storageService;
    private final TextExtractionService textExtractionService;

    @Transactional
    public ResumeResponse uploadResume(User user, MultipartFile file) {
        String filePath = storageService.store(file);
        String text = textExtractionService.extractText(file);
        Resume resume = Resume.builder()
                .user(user)
                .name(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath)
                .extractedText(text)
                .build();
        Resume saved = resumeRepository.save(resume);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ResumeResponse> listResumes(User user) {
        return resumeRepository.findAllByUser(user).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Resume getResume(User user, Long id) {
        return resumeRepository.findById(id)
                .filter(resume -> resume.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));
    }

    @Transactional
    public void deleteResume(User user, Long id) {
        Resume resume = getResume(user, id);
        resumeRepository.delete(resume);
        storageService.delete(resume.getFilePath());
    }

    public ResumeResponse toResponse(Resume resume) {
        return ResumeResponse.builder()
                .id(resume.getId())
                .name(resume.getName())
                .fileType(resume.getFileType())
                .createdAt(resume.getCreatedAt())
                .updatedAt(resume.getUpdatedAt())
                .build();
    }
}

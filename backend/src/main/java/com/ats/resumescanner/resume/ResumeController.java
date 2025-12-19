package com.ats.resumescanner.resume;

import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.resume.dto.ResumeResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<ResumeResponse> upload(@AuthenticationPrincipal User user,
                                                 @RequestPart("file") @NotNull MultipartFile file) {
        return ResponseEntity.ok(resumeService.uploadResume(user, file));
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> list(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(resumeService.listResumes(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResumeResponse> get(@AuthenticationPrincipal User user, @PathVariable Long id) {
        return ResponseEntity.ok(resumeService.toResponse(resumeService.getResume(user, id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal User user, @PathVariable Long id) {
        resumeService.deleteResume(user, id);
        return ResponseEntity.noContent().build();
    }
}

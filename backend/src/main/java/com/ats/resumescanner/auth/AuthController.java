package com.ats.resumescanner.auth;

import com.ats.resumescanner.auth.dto.AuthResponse;
import com.ats.resumescanner.auth.dto.LoginRequest;
import com.ats.resumescanner.auth.dto.RegisterRequest;
import com.ats.resumescanner.auth.service.AuthService;
import com.ats.resumescanner.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> passwordResetStub(@RequestParam String email) {
        return ResponseEntity.ok("Password reset link stub for " + email);
    }
}

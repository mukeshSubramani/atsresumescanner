package com.ats.resumescanner.auth.service;

import com.ats.resumescanner.auth.Role;
import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.auth.UserRepository;
import com.ats.resumescanner.auth.dto.AuthResponse;
import com.ats.resumescanner.auth.dto.LoginRequest;
import com.ats.resumescanner.auth.dto.RegisterRequest;
import com.ats.resumescanner.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        User user = userService.createUser(request.getEmail(), request.getPassword(), Role.USER);
        String token = jwtService.generateToken(user, Map.of("role", user.getRole().name()));
        return AuthResponse.builder()
                .token(token)
                .role(user.getRole().name())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            User user = (User) authentication.getPrincipal();
            String token = jwtService.generateToken(user, Map.of("role", user.getRole().name()));
            return AuthResponse.builder()
                    .token(token)
                    .role(user.getRole().name())
                    .build();
        } catch (AuthenticationException ex) {
            throw ex;
        }
    }

    public User loadUser(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }
}

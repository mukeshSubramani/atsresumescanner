package com.ats.resumescanner.auth.service;

import com.ats.resumescanner.auth.Role;
import com.ats.resumescanner.auth.User;
import com.ats.resumescanner.auth.UserRepository;
import com.ats.resumescanner.auth.dto.MeResponse;
import com.ats.resumescanner.common.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User createUser(String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email already registered");
        }
        User user = User.builder()
                .email(email)
                .passwordHash(passwordEncoder.encode(password))
                .role(role)
                .build();
        return userRepository.save(user);
    }

    public MeResponse toMeResponse(User user) {
        return MeResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}

package com.hotel.service;

import com.hotel.config.JwtUtil;
import com.hotel.dto.*;
import com.hotel.entity.User;
import com.hotel.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authManager;
    private final EmailService emailService;

    public AuthResponse login(LoginRequest req) {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }
        User user = userRepo.findByEmail(req.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));
        return buildResponse(user);
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepo.existsByEmail(req.getEmail()))
            throw new RuntimeException("Email already registered");
        User user = userRepo.save(User.builder()
                .name(req.getName()).email(req.getEmail())
                .password(encoder.encode(req.getPassword()))
                .phone(req.getPhone()).role(User.Role.USER).build());
        emailService.sendWelcomeEmail(user.getEmail(), user.getName());
        return buildResponse(user);
    }

    private AuthResponse buildResponse(User user) {
        return AuthResponse.builder()
                .token(jwtUtil.generateToken(user))
                .role(user.getRole().name())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail()).build();
    }
}

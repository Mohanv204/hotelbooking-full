package com.example.hotelbooking.service;

import com.example.hotelbooking.dto.AuthDtos;
import com.example.hotelbooking.entity.Role;
import com.example.hotelbooking.entity.User;
import com.example.hotelbooking.repository.UserRepository;
import com.example.hotelbooking.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String register(AuthDtos.RegisterRequest request) {
        userRepository.findByEmail(request.email()).ifPresent(u -> {
            throw new ResponseStatusException(BAD_REQUEST, "Email is already registered.");
        });

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);

        return jwtForUser(user);
    }

    public String signIn(AuthDtos.SignInRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid email or password."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password.");
        }

        return jwtForUser(user);
    }

    private String jwtForUser(User user) {
        List<String> roles = List.of(user.getRole().name());
        return jwtUtil.generateToken(user.getId(), user.getEmail(), roles);
    }
}


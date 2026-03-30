package com.example.hotelbooking.web;

import com.example.hotelbooking.dto.AuthDtos;
import com.example.hotelbooking.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest request) {
        String token = authService.register(request);
        return new AuthDtos.AuthResponse(token);
    }

    @PostMapping("/signin")
    public AuthDtos.AuthResponse signIn(@Valid @RequestBody AuthDtos.SignInRequest request) {
        String token = authService.signIn(request);
        return new AuthDtos.AuthResponse(token);
    }
}


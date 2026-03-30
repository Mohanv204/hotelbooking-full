package com.hotelbooking.app.hotelbooking.controller;

import com.hotelbooking.app.hotelbooking.dto.LoginRequest;
import com.hotelbooking.app.hotelbooking.dto.RegisterRequest;
import com.hotelbooking.app.hotelbooking.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//AuthController.java
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

 private final AuthService authService;

 public AuthController(AuthService authService) {
     this.authService = authService;
 }

 @PostMapping("/register")
 public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
     return ResponseEntity.ok(authService.register(request));
 }

 @PostMapping("/login")
 public ResponseEntity<String> login(@RequestBody LoginRequest request) {
     return ResponseEntity.ok(authService.login(request));
 }
}
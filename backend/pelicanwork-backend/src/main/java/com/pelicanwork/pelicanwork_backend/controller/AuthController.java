package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.*;
import com.pelicanwork.pelicanwork_backend.entity.User;
import com.pelicanwork.pelicanwork_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        try {
            User user = userService.registerUser(request);
            
            AuthResponse response = new AuthResponse(
                "User registered successfully!",
                user.getId(),
                user.getEmail()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            AuthResponse response = new AuthResponse(
                e.getMessage(),
                null,
                null
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            String token = userService.loginUser(request);
            User user = userService.findByEmail(request.getEmail());
            
            LoginResponse response = new LoginResponse(
                token,
                user.getEmail(),
                user.getId(),
                user.getFirstName(),
                user.getLastName()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            LoginResponse response = new LoginResponse(
                null,
                null,
                null,
                null,
                null
            );
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getCurrentUser() {
        AuthResponse response = new AuthResponse(
            "Authentication endpoint - User context will be available after JWT filter implementation",
            null,
            null
        );
        return ResponseEntity.ok(response);
    }
}

package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.dto.*;
import com.pelicanwork.pelicanwork_backend.entity.User;
import com.pelicanwork.pelicanwork_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * POST /api/auth/register
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody AuthRequest request) {
        try {
            System.out.println("Registering user: " + request.getEmail());

            User user = userService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getName(),
                request.getPhone()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "User registered successfully");
            response.put("data", new AuthResponse(user.getId(), user.getEmail(), user.getName()));

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("Error registering user: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to register user: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * POST /api/auth/login
     * Login user and return JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthRequest request) {
        try {
            System.out.println("Logging in user: " + request.getEmail());

            AuthResponse authResponse = userService.authenticateUser(
                request.getEmail(),
                request.getPassword()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("status", "SUCCESS");
            response.put("message", "Login successful");
            response.put("data", authResponse);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("Error logging in user: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to login: " + e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    /**
     * GET /api/auth/me
     * Get current authenticated user
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "SUCCESS");
        response.put("message", "Authentication endpoint - User context will be available after JWT filter implementation");
        return ResponseEntity.ok(response);
    }
}

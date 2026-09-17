package com.pelicanwork.pelicanwork_backend.dto;

public class AuthResponse {
    private String message;
    private Long userId;
    private String email;

    public AuthResponse() {}

    public AuthResponse(String message, Long userId, String email) {
        this.message = message;
        this.userId = userId;
        this.email = email;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}

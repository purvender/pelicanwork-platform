package com.pelicanwork.pelicanwork_backend.dto;

public class AuthResponse {
    private Long id;
    private String email;
    private String name;
    private String token;

    // No-args constructor (required for JSON deserialization)
    public AuthResponse() {}

    // Constructor with 3 parameters (id, email, name)
    public AuthResponse(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    // Constructor with 4 parameters (id, email, name, token)
    public AuthResponse(Long id, String email, String name, String token) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.token = token;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}

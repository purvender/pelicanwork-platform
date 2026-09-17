package com.pelicanwork.pelicanwork_backend.dto;

public class LoginResponse {
    private String token;
    private String email;
    private Long userId;
    private String firstName;
    private String lastName;

    public LoginResponse() {}

    public LoginResponse(String token, String email, Long userId, String firstName, String lastName) {
        this.token = token;
        this.email = email;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
}

package com.pelicanwork.pelicanwork_backend.controller;

import com.pelicanwork.pelicanwork_backend.entity.User;
import com.pelicanwork.pelicanwork_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class DatabaseTestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/db")
    public Map<String, Object> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            long count = userRepository.count();
            response.put("status", "SUCCESS");
            response.put("message", "Database connected!");
            response.put("userCount", count);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Database error: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            User user = new User();
            user.setEmail(data.get("email"));
            user.setPassword(data.get("password"));
            user.setFirstName(data.get("firstName"));
            user.setLastName(data.get("lastName"));
            
            User saved = userRepository.save(user);
            
            response.put("status", "SUCCESS");
            response.put("message", "User created!");
            response.put("userId", saved.getId());
            response.put("email", saved.getEmail());
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/users")
    public Map<String, Object> getAllUsers() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<User> users = userRepository.findAll();
            response.put("status", "SUCCESS");
            response.put("count", users.size());
            response.put("users", users);
        } catch (Exception e) {
            response.put("status", "ERROR");
            response.put("message", "Error: " + e.getMessage());
        }
        return response;
    }
}

package com.pelicanwork.pelicanwork_backend.service;

import com.pelicanwork.pelicanwork_backend.dto.AuthResponse;
import com.pelicanwork.pelicanwork_backend.entity.User;
import com.pelicanwork.pelicanwork_backend.repository.UserRepository;
import com.pelicanwork.pelicanwork_backend.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Register a new user
     */
    public User registerUser(String email, String password, String name, String phone) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("User with email " + email + " already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setPhone(phone);

        return userRepository.save(user);
    }

    /**
     * Authenticate user and return JWT token
     */
    public AuthResponse authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(user.getId(), user.getEmail(), user.getName(), token);
    }

    /**
     * Get user by ID
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    /**
     * Get user by email
     */
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    /**
     * Update user profile (name, phone, addresses)
     */
    public User updateUserProfile(Long userId, String name, String phone, 
                                   String shippingAddress, String billingAddress) {
        User user = getUserById(userId);

        if (name != null && !name.trim().isEmpty()) {
            user.setName(name.trim());
        }

        if (phone != null) {
            user.setPhone(phone.trim());
        }

        if (shippingAddress != null) {
            user.setShippingAddress(shippingAddress.trim());
        }

        if (billingAddress != null) {
            user.setBillingAddress(billingAddress.trim());
        }

        return userRepository.save(user);
    }

    /**
     * Update user shipping address
     */
    public User updateShippingAddress(Long userId, String shippingAddress) {
        User user = getUserById(userId);
        user.setShippingAddress(shippingAddress);
        return userRepository.save(user);
    }

    /**
     * Update user billing address
     */
    public User updateBillingAddress(Long userId, String billingAddress) {
        User user = getUserById(userId);
        user.setBillingAddress(billingAddress);
        return userRepository.save(user);
    }
}

package com.urlshortener.controller;

import com.urlshortener.entity.User;
import com.urlshortener.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
import java.util.UUID;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/api/v1/register")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> registerUser(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(credentials.get("password")));
        user.setApiKey(UUID.randomUUID().toString());

        userRepository.save(user);

        return Map.of("apiKey", user.getApiKey());
    }
}
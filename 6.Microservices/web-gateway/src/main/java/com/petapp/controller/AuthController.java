package com.petapp.controller;

import com.petapp.dto.UserDto;
import com.petapp.entities.User;
import com.petapp.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Register a new user", description = "Creating a new user. Only accessible to ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDto userDto) {
        try {
            if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
            }
            User user = new User();
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            user.setRole(userDto.getRole());
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok("User registered successfully, ID: " + savedUser.getId());
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Database error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error: " + e.getMessage());
        }
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users. Only accessible to ADMIN.")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDto> userDtos = users.stream()
                    .map(user -> {
                        UserDto dto = new UserDto();
                        dto.setId(user.getId());
                        dto.setUsername(user.getUsername());
                        dto.setRole(user.getRole());
                        return dto;
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(userDtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
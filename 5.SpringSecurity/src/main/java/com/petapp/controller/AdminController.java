package com.petapp.controller;

import com.petapp.dto.UserDto;
import com.petapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }
}
package com.petapp.service;

import com.petapp.dto.OwnerDto;
import com.petapp.dto.UserDto;
import com.petapp.entities.User;
import com.petapp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final OwnerService ownerService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(OwnerService ownerService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.ownerService = ownerService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole("ROLE_" + userDto.getRole().toUpperCase());
        User savedUser = userRepository.save(user);

        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setName(userDto.getUsername());
        ownerDto.setUserId(savedUser.getId());
        ownerService.createOwner(ownerDto);

        return userDto;
    }
}
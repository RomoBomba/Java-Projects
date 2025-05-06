package com.petapp.service;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import com.petapp.entities.User;
import com.petapp.mapper.OwnerMapper;
import com.petapp.repository.OwnerRepository;
import com.petapp.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final UserRepository userRepository;

    public OwnerService(OwnerRepository ownerRepository, OwnerMapper ownerMapper, UserRepository userRepository) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.userRepository = userRepository;
    }

    public List<OwnerDto> getAllOwners() {
        return ownerRepository.findAllWithPets()
                .stream()
                .map(ownerMapper::toDto)
                .collect(Collectors.toList());
    }

    public OwnerDto getOwnerById(Long id) {
        Owner owner = ownerRepository.findByIdWithPetsAndUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));
        return ownerMapper.toDto(owner);
    }

    public OwnerDto createOwner(OwnerDto ownerDto) {
        Owner owner = ownerMapper.toEntity(ownerDto);
        if (ownerDto.getUserId() != null) {
            User user = userRepository.findById(ownerDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + ownerDto.getUserId()));
            owner.setUser(user);
        }
        Owner savedOwner = ownerRepository.save(owner);
        return ownerMapper.toDto(savedOwner);
    }

    public OwnerDto updateOwner(Long id, OwnerDto ownerDto) {
        Owner owner = ownerRepository.findByIdWithPetsAndUser(id)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + id));

        owner.setName(ownerDto.getName());
        owner.setBirthDate(ownerDto.getBirthDate());
        if (ownerDto.getUserId() != null) {
            User user = userRepository.findById(ownerDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + ownerDto.getUserId()));
            owner.setUser(user);
        }
        Owner updated = ownerRepository.save(owner);

        return ownerMapper.toDto(updated);
    }

    public void deleteOwner(Long id) {
        if (!ownerRepository.existsById(id)) {
            throw new EntityNotFoundException("Owner not found with id: " + id);
        }
        ownerRepository.deleteById(id);
    }
}
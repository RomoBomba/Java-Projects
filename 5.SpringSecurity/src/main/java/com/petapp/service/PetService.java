package com.petapp.service;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapp.mapper.PetMapper;
import com.petapp.repository.OwnerRepository;
import com.petapp.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {

    private final PetRepository petRepository;
    private final OwnerRepository ownerRepository;
    private final PetMapper petMapper;

    public PetService(PetRepository petRepository, OwnerRepository ownerRepository, PetMapper petMapper) {
        this.petRepository = petRepository;
        this.ownerRepository = ownerRepository;
        this.petMapper = petMapper;
    }

    public List<PetDto> getAllPets() {
        return petRepository.findAll()
                .stream()
                .map(petMapper::toDto)
                .collect(Collectors.toList());
    }

    public PetDto getPetById(Long id) {
        Pet pet = petRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));
        return petMapper.toDto(pet);
    }

    public PetDto createPet(CreatePetRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Owner owner = ownerRepository.findByUserUsernameWithUser(username)
                .orElseThrow(() -> new EntityNotFoundException("Owner not found for user: " + username));

        if (!owner.getId().equals(request.getOwnerId()) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You are not authorized to create a pet for this owner");
        }

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setBirthDate(request.getBirthDate());
        pet.setBreed(request.getBreed());
        pet.setColor(request.getColor());
        pet.setOwner(owner);

        Pet saved = petRepository.save(pet);
        return petMapper.toDto(saved);
    }

    public PetDto updatePet(Long id, PetDto petDto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pet pet = petRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));

        if (!pet.getOwner().getUser().getUsername().equals(username) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You are not authorized to update this pet");
        }

        pet.setName(petDto.getName());
        pet.setBirthDate(petDto.getBirthDate());
        pet.setBreed(petDto.getBreed());
        pet.setColor(petDto.getColor());

        Pet updated = petRepository.save(pet);
        return petMapper.toDto(updated);
    }

    public void deletePet(Long id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Pet pet = petRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));

        if (!pet.getOwner().getUser().getUsername().equals(username) &&
                !SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                        .stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            throw new AccessDeniedException("You are not authorized to delete this pet");
        }

        petRepository.deleteById(id);
    }
}
package com.petapp.service;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapp.mapper.PetMapper;
import com.petapp.repository.OwnerRepository;
import com.petapp.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
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
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));
        return petMapper.toDto(pet);
    }

    public PetDto createPet(CreatePetRequest request) {
        Owner owner = ownerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found with id: " + request.getOwnerId()));

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
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + id));

        pet.setName(petDto.getName());
        pet.setBirthDate(petDto.getBirthDate());
        pet.setBreed(petDto.getBreed());
        pet.setColor(petDto.getColor());

        Pet updated = petRepository.save(pet);
        return petMapper.toDto(updated);
    }

    public void deletePet(Long id) {
        if (!petRepository.existsById(id)) {
            throw new EntityNotFoundException("Pet not found with id: " + id);
        }
        petRepository.deleteById(id);
    }
}
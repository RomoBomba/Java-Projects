package com.petapp.mapper;

import com.petapp.dto.PetDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

    public PetDto toDto(Pet pet) {
        PetDto dto = new PetDto();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setBirthDate(pet.getBirthDate());
        dto.setBreed(pet.getBreed());
        dto.setColor(pet.getColor());
        dto.setOwnerId(pet.getOwner().getId());
        return dto;
    }

    public Pet toEntity(PetDto dto, Owner owner) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setName(dto.getName());
        pet.setBirthDate(dto.getBirthDate());
        pet.setBreed(dto.getBreed());
        pet.setColor(dto.getColor());
        pet.setOwner(owner);
        return pet;
    }
}
package com.petapp.mapper;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OwnerMapper {

    public OwnerDto toDto(Owner owner) {
        OwnerDto dto = new OwnerDto();
        dto.setId(owner.getId());
        dto.setName(owner.getName());
        dto.setBirthDate(owner.getBirthDate());

        dto.setPetIds(owner.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList()));

        return dto;
    }

    public Owner toEntity(OwnerDto dto) {
        Owner owner = new Owner();
        owner.setId(dto.getId());
        owner.setName(dto.getName());
        owner.setBirthDate(dto.getBirthDate());
        return owner;
    }
}
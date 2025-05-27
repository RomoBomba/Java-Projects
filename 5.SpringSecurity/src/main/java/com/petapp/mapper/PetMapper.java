package com.petapp.mapper;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    PetDto toDto(Pet pet);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "friends", ignore = true)
    Pet toEntity(CreatePetRequest request);

    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "friends", ignore = true)
    Pet toEntity(PetDto dto);
}
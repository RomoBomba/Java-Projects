package com.petapp.mapper;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(target = "ownerId", source = "ownerId")
    PetDto toDto(Pet entity);

    @Mapping(target = "ownerId", source = "ownerId")
    Pet toEntity(PetDto dto);

    @Mapping(target = "ownerId", source = "ownerId")
    Pet toEntity(CreatePetRequest request);

    @Mapping(target = "id", ignore = true)
    void updateEntity(@MappingTarget Pet entity, PetDto dto);
}
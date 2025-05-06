package com.petapp.mapper;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OwnerMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "petIds", source = "pets", qualifiedByName = "mapPetIds")
    OwnerDto toDto(Owner owner);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "pets", ignore = true)
    Owner toEntity(OwnerDto ownerDto);

    @Named("mapPetIds")
    default List<Long> mapPetIds(List<Pet> pets) {
        return pets != null ? pets.stream()
                .map(Pet::getId)
                .collect(Collectors.toList()) : Collections.emptyList();
    }
}
package com.petapp.mapper;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface OwnerMapper {
    Owner toEntity(OwnerDto dto);
    OwnerDto toDto(Owner entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(OwnerDto dto, @MappingTarget Owner entity);
}
package com.petapp.mapper;

import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-26T17:32:20+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class OwnerMapperImpl implements OwnerMapper {

    @Override
    public Owner toEntity(OwnerDto dto) {
        if ( dto == null ) {
            return null;
        }

        Owner owner = new Owner();

        owner.setId( dto.getId() );
        owner.setName( dto.getName() );
        owner.setBirthDate( dto.getBirthDate() );
        owner.setUserId( dto.getUserId() );

        return owner;
    }

    @Override
    public OwnerDto toDto(Owner entity) {
        if ( entity == null ) {
            return null;
        }

        OwnerDto ownerDto = new OwnerDto();

        ownerDto.setId( entity.getId() );
        ownerDto.setName( entity.getName() );
        ownerDto.setBirthDate( entity.getBirthDate() );
        ownerDto.setUserId( entity.getUserId() );

        return ownerDto;
    }

    @Override
    public void updateEntity(OwnerDto dto, Owner entity) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            entity.setId( dto.getId() );
        }
        if ( dto.getName() != null ) {
            entity.setName( dto.getName() );
        }
        if ( dto.getBirthDate() != null ) {
            entity.setBirthDate( dto.getBirthDate() );
        }
        if ( dto.getUserId() != null ) {
            entity.setUserId( dto.getUserId() );
        }
    }
}

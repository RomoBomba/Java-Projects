package com.petapp.mapper;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Pet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-26T17:32:02+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 23.0.1 (Oracle Corporation)"
)
@Component
public class PetMapperImpl implements PetMapper {

    @Override
    public PetDto toDto(Pet entity) {
        if ( entity == null ) {
            return null;
        }

        PetDto petDto = new PetDto();

        petDto.setOwnerId( entity.getOwnerId() );
        petDto.setId( entity.getId() );
        petDto.setName( entity.getName() );
        petDto.setBirthDate( entity.getBirthDate() );
        petDto.setBreed( entity.getBreed() );
        petDto.setColor( entity.getColor() );

        return petDto;
    }

    @Override
    public Pet toEntity(PetDto dto) {
        if ( dto == null ) {
            return null;
        }

        Pet pet = new Pet();

        pet.setOwnerId( dto.getOwnerId() );
        pet.setId( dto.getId() );
        pet.setName( dto.getName() );
        pet.setBirthDate( dto.getBirthDate() );
        pet.setBreed( dto.getBreed() );
        pet.setColor( dto.getColor() );

        return pet;
    }

    @Override
    public Pet toEntity(CreatePetRequest request) {
        if ( request == null ) {
            return null;
        }

        Pet pet = new Pet();

        pet.setOwnerId( request.getOwnerId() );
        pet.setName( request.getName() );
        pet.setBirthDate( request.getBirthDate() );
        pet.setBreed( request.getBreed() );
        pet.setColor( request.getColor() );

        return pet;
    }

    @Override
    public void updateEntity(Pet entity, PetDto dto) {
        if ( dto == null ) {
            return;
        }

        entity.setName( dto.getName() );
        entity.setBirthDate( dto.getBirthDate() );
        entity.setBreed( dto.getBreed() );
        entity.setColor( dto.getColor() );
        entity.setOwnerId( dto.getOwnerId() );
    }
}

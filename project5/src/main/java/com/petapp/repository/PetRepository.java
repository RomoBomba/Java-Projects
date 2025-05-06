package com.petapp.repository;

import com.petapp.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet, Long> {
    @Query("SELECT p FROM Pet p JOIN FETCH p.owner WHERE p.id = :id")
    Optional<Pet> findByIdWithOwner(Long id);
}
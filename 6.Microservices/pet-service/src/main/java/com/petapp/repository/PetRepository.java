package com.petapp.repository;

import com.petapp.entities.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
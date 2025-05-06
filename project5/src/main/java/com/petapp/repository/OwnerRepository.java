package com.petapp.repository;

import com.petapp.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
    @Query("SELECT o FROM Owner o LEFT JOIN FETCH o.pets LEFT JOIN FETCH o.user")
    List<Owner> findAllWithPets();

    @Query("SELECT o FROM Owner o JOIN FETCH o.pets JOIN FETCH o.user WHERE o.id = :id")
    Optional<Owner> findByIdWithPetsAndUser(Long id);

    @Query("SELECT o FROM Owner o JOIN FETCH o.user WHERE o.user.username = :username")
    Optional<Owner> findByUserUsernameWithUser(String username);

    @Query("SELECT o FROM Owner o JOIN FETCH o.pets p JOIN FETCH o.user WHERE p.id = :petId")
    Optional<Owner> findOwnerByPetId(Long petId);
}
package com.petapp.repository;

import com.petapp.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
package com.petapp.service;

import com.petapp.entities.Owner;
import com.petapp.repository.OwnerRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("ownerSecurity")
public class OwnerSecurity {

    private final OwnerRepository ownerRepository;

    public OwnerSecurity(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    public boolean isPetOwner(Authentication authentication, Long petId) {
        String username = authentication.getName();

        Owner owner = ownerRepository.findOwnerByPetId(petId)
                .orElseThrow(() -> new RuntimeException("Owner not found for petId: " + petId));

        return owner.getUser().getUsername().equals(username);
    }
}
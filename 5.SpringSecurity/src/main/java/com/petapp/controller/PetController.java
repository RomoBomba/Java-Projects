package com.petapp.controller;

import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@SecurityRequirement(name = "basicAuth")
public class PetController {
    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PetDto>> getAllPets() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetDto> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @Operation(summary = "Create a pet", description = "Requires USER or ADMIN role")
    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<PetDto> createPet(@RequestBody CreatePetRequest request) {
        return ResponseEntity.ok(petService.createPet(request));
    }

    @Operation(summary = "Update a pet", description = "Requires ownership or ADMIN role")
    @PutMapping("/{id}")
    @PreAuthorize("@ownerSecurity.isPetOwner(authentication, #id) or hasRole('ADMIN')")
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id, @RequestBody PetDto petDto) {
        try {
            return ResponseEntity.ok(petService.updatePet(id, petDto));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Delete a pet", description = "Requires ownership or ADMIN role")
    @DeleteMapping("/{id}")
    @PreAuthorize("@ownerSecurity.isPetOwner(authentication, #id) or hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
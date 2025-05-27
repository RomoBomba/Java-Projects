package com.petapp.controller;

import com.petapp.dto.OwnerDto;
import com.petapp.service.OwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/owners")
public class OwnerController {

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<OwnerDto> getOwnerById(@PathVariable Long id) {
        return ResponseEntity.ok(ownerService.getOwnerById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OwnerDto> createOwner(@RequestBody OwnerDto ownerDto) {
        return ResponseEntity.ok(ownerService.createOwner(ownerDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("@ownerSecurity.isOwner(authentication, #id) or hasRole('ADMIN')")
    public ResponseEntity<OwnerDto> updateOwner(@PathVariable Long id,
                                                @RequestBody OwnerDto ownerDto) {
        return ResponseEntity.ok(ownerService.updateOwner(id, ownerDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOwner(@PathVariable Long id) {
        ownerService.deleteOwner(id);
        return ResponseEntity.noContent().build();
    }
}
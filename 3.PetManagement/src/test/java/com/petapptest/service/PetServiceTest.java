package com.petapptest.service;

import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapp.service.OwnerService;
import com.petapp.service.PetService;
import com.petapptest.AbstractTestcontainersTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PetServiceTest extends AbstractTestcontainersTest {

    private final PetService petService = new PetService();
    private final OwnerService ownerService = new OwnerService();

    @Test
    void createUpdateAndDeletePet() {
        Owner owner = new Owner();
        owner.setName("Service Pet Owner");
        owner.setBirthDate("1995-05-05");
        Owner savedOwner = ownerService.createOwner(owner);
        assertNotNull(savedOwner.getId());

        Pet pet = new Pet();
        pet.setName("Test Pet");
        pet.setBreed("Golden Retriever");
        pet.setColor("Golden");
        pet.setOwner(savedOwner);

        Pet savedPet = petService.createPet(pet);
        assertNotNull(savedPet.getId());
        assertEquals("Test Pet", savedPet.getName());

        savedPet.setName("Updated Pet");
        Pet updatedPet = petService.updatePet(savedPet);
        assertEquals("Updated Pet", updatedPet.getName());

        petService.deletePetById(updatedPet.getId());
        assertNull(petService.getPetById(updatedPet.getId()));
    }
}
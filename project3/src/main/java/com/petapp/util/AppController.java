package com.petapp.util;

import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapp.service.OwnerService;
import com.petapp.service.PetService;

public class AppController {
    private final OwnerService ownerService = new OwnerService();
    private final PetService petService = new PetService();

    public void run() {
        Owner owner = new Owner();
        owner.setName("Romka");
        owner.setBirthDate("2006-01-24");

        Pet pet = new Pet();
        pet.setName("Timoha");
        pet.setBirthDate("2020-06-15");
        pet.setBreed("Siamese");
        pet.setColor("Black");
        pet.setOwner(owner);

        try {
            ownerService.createOwner(owner);
            petService.createPet(pet);
            System.out.println("Созданы владелец и питомец!");
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
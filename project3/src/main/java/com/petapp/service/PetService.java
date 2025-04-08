package com.petapp.service;

import com.petapp.dao.PetDao;
import com.petapp.entities.Pet;
import java.util.List;

public class PetService {
    private final PetDao petDao = new PetDao();

    public Pet createPet(Pet pet) {
        return petDao.save(pet);
    }

    public Pet updatePet(Pet pet) {
        return petDao.update(pet);
    }

    public Pet getPetById(long id) {
        return petDao.getById(id);
    }

    public List<Pet> getAllPets() {
        return petDao.getAll();
    }

    public void deletePetById(long id) {
        petDao.deleteById(id);
    }

    public void deletePet(Pet pet) {
        petDao.deleteByEntity(pet);
    }

    public void deleteAllPets() {
        petDao.deleteAll();
    }
}

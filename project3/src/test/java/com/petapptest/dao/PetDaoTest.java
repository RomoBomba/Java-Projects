package com.petapptest.dao;

import com.petapp.dao.OwnerDao;
import com.petapp.dao.PetDao;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapptest.AbstractTestcontainersTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PetDaoTest extends AbstractTestcontainersTest {

    private final PetDao petDao = new PetDao();
    private final OwnerDao ownerDao = new OwnerDao();

    @Test
    void saveAndGetPet() {
        Owner owner = new Owner();
        owner.setName("Pet Owner");
        ownerDao.save(owner);

        Pet pet = new Pet();
        pet.setName("Rex");
        pet.setOwner(owner);

        Pet saved = petDao.save(pet);
        assertNotNull(saved.getId());

        Pet fromDb = petDao.getById(saved.getId());
        assertEquals("Rex", fromDb.getName());
        assertEquals(owner.getId(), fromDb.getOwner().getId());
    }
}
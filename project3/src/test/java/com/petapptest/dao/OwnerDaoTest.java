package com.petapptest.dao;

import com.petapp.dao.OwnerDao;
import com.petapp.entities.Owner;
import com.petapptest.AbstractTestcontainersTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OwnerDaoTest extends AbstractTestcontainersTest {

    private final OwnerDao ownerDao = new OwnerDao();

    @Test
    void saveAndGetOwner() {
        Owner owner = new Owner();
        owner.setName("Test Containers Owner");
        owner.setBirthDate("2000-01-01");

        Owner saved = ownerDao.save(owner);
        assertNotNull(saved.getId());

        Owner fromDb = ownerDao.getById(saved.getId());
        assertEquals("Test Containers Owner", fromDb.getName());
    }
}
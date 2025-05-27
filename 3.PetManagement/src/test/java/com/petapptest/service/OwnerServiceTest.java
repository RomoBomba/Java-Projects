package com.petapptest.service;

import com.petapp.entities.Owner;
import com.petapp.service.OwnerService;
import com.petapptest.AbstractTestcontainersTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class OwnerServiceTest extends AbstractTestcontainersTest {

    private final OwnerService ownerService = new OwnerService();

    @Test
    void createAndDeleteOwner() {
        Owner owner = new Owner();
        owner.setName("Service Test");

        Owner saved = ownerService.createOwner(owner);
        assertNotNull(saved.getId());

        ownerService.deleteOwnerById(saved.getId());
        assertNull(ownerService.getOwnerById(saved.getId()));
    }
}
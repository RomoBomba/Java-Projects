package com.petapp.service;

import com.petapp.dao.OwnerDao;
import com.petapp.entities.Owner;
import java.util.List;

public class OwnerService {
    private final OwnerDao ownerDao = new OwnerDao();

    public Owner createOwner(Owner owner) {
        return ownerDao.save(owner);
    }

    public Owner updateOwner(Owner owner) {
        return ownerDao.update(owner);
    }

    public Owner getOwnerById(long id) {
        return ownerDao.getById(id);
    }

    public List<Owner> getAllOwners() {
        return ownerDao.getAll();
    }

    public void deleteOwnerById(long id) {
        ownerDao.deleteById(id);
    }

    public void deleteOwner(Owner owner) {
        ownerDao.deleteByEntity(owner);
    }

    public void deleteAllOwners() {
        ownerDao.deleteAll();
    }
}

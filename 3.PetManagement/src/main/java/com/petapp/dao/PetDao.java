package com.petapp.dao;

import com.petapp.entities.Pet;
import com.petapp.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class PetDao implements IDao<Pet> {
    @Override
    public Pet save(Pet pet) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(pet);
            transaction.commit();
            return pet;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void deleteById(long id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Pet pet = session.get(Pet.class, id);
            if (pet != null) {
                session.delete(pet);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByEntity(Pet pet) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(pet);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            List<Pet> pets = getAll();
            for (Pet pet : pets) {
                session.delete(pet);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Pet update(Pet pet) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(pet);
            transaction.commit();
            return pet;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Pet getById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Pet.class, id);
        }
    }

    @Override
    public List<Pet> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Pet", Pet.class).list();
        }
    }
}
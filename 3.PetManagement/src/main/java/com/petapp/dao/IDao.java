package com.petapp.dao;
import java.util.List;

public interface IDao<T> {
    T save(T entity);
    void deleteById(long id);
    void deleteByEntity(T entity);
    void deleteAll();
    T update(T entity);
    T getById(long id);
    List<T> getAll();
}
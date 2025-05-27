package com.petapp.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "owners")
public class Owner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "user_id")
    private Long userId;

    public Owner() {
    }

    public Owner(String name, String birthDate, Long userId) {
        this.name = name;
        this.birthDate = birthDate;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
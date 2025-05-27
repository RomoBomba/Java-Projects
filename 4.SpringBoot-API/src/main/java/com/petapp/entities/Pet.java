package com.petapp.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pets")
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "birth_date")
    private String birthDate;

    private String breed;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    @ManyToMany
    @JoinTable(
            name = "pet_friends",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Pet> friends = new ArrayList<>();

    public Pet() {
    }

    public Pet(String name, String birthDate, String breed, String color, Owner owner) {
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
    }

    public Pet(Long id, String name, String birthDate, String breed, String color, Owner owner) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.color = color;
        this.owner = owner;
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

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Pet> getFriends() {
        return friends;
    }

    public void setFriends(List<Pet> friends) {
        this.friends = friends;
    }

    public void addFriend(Pet friend) {
        friends.add(friend);
        friend.getFriends().add(this);
    }

    public void removeFriend(Pet friend) {
        friends.remove(friend);
        friend.getFriends().remove(this);
    }
}
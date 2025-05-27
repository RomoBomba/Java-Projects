package com.petapp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreatePetRequest {
    @NotBlank
    private String name;

    @NotNull
    private String birthDate;

    @NotBlank
    private String breed;

    @NotBlank
    private String color;

    @NotNull
    private Long ownerId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public String getBreed() { return breed; }
    public void setBreed(String breed) { this.breed = breed; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
}
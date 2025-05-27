package com.petapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class OwnerDto {
    private Long id;

    @JsonProperty(required = true)
    private String name;

    @JsonProperty(required = true)
    private String birthDate;

    @JsonProperty(required = true)
    private Long userId;

    @JsonIgnore
    private List<Long> petIds;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<Long> getPetIds() { return petIds; }
    public void setPetIds(List<Long> petIds) { this.petIds = petIds; }
}
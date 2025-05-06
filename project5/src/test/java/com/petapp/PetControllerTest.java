package com.petapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.dto.CreatePetRequest;
import com.petapp.dto.PetDto;
import com.petapp.entities.Owner;
import com.petapp.entities.Pet;
import com.petapp.entities.User;
import com.petapp.repository.OwnerRepository;
import com.petapp.repository.PetRepository;
import com.petapp.repository.UserRepository;
import com.petapp.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PetService petService;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    private Long petId;
    private Long ownerId;

    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
        ownerRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("testUser");
        user.setPassword(passwordEncoder.encode("testPass"));
        user.setRole("ROLE_USER");
        user = userRepository.save(user);

        Owner owner = new Owner();
        owner.setName("Test Owner");
        owner.setBirthDate("2000-01-01");
        owner.setUser(user);
        owner = ownerRepository.save(owner);

        ownerId = owner.getId();

        Pet pet = new Pet();
        pet.setName("Fluffy");
        pet.setBirthDate(LocalDate.of(2023, 1, 1).toString());
        pet.setBreed("Cat");
        pet.setColor("White");
        pet.setOwner(owner);

        pet = petRepository.save(pet);
        petId = pet.getId();
    }

    @Test
    @WithMockUser(username = "user", authorities = {"ROLE_USER"})
    void testUpdateNotOwnPetAsUser() throws Exception {
        User otherUser = new User();
        otherUser.setUsername("otherUser");
        otherUser.setPassword(passwordEncoder.encode("otherPass"));
        otherUser.setRole("ROLE_USER");
        otherUser = userRepository.save(otherUser);

        Owner otherOwner = new Owner();
        otherOwner.setName("Other Owner");
        otherOwner.setBirthDate("2010-01-01");
        otherOwner.setUser(otherUser);
        otherOwner = ownerRepository.save(otherOwner);

        Pet otherPet = new Pet();
        otherPet.setName("OtherPet");
        otherPet.setBirthDate(LocalDate.now().toString());
        otherPet.setBreed("Dog");
        otherPet.setColor("Black");
        otherPet.setOwner(otherOwner);
        otherPet = petRepository.save(otherPet);

        PetDto petDto = new PetDto();
        petDto.setName("Updated Pet");

        mockMvc.perform(put("/api/pets/" + otherPet.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petDto)))
                .andExpect(status().isForbidden());
    }
}
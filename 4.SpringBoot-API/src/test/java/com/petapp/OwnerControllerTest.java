package com.petapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.controller.OwnerController;
import com.petapp.dto.OwnerDto;
import com.petapp.service.OwnerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OwnerController.class)
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetOwnerById() throws Exception {
        Long ownerId = 1L;
        OwnerDto ownerDto = new OwnerDto();
        ownerDto.setId(ownerId);
        ownerDto.setName("Vasya");
        ownerDto.setBirthDate("1900-11-30");

        when(ownerService.getOwnerById(ownerId)).thenReturn(ownerDto);

        mockMvc.perform(get("/api/owners/{id}", ownerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ownerId))
                .andExpect(jsonPath("$.name").value("Vasya"))
                .andExpect(jsonPath("$.birthDate").value("1900-11-30"));
    }
}
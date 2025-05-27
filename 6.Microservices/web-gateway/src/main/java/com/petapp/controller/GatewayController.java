package com.petapp.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.dto.CommandMessage;
import com.petapp.dto.OwnerDto;
import com.petapp.dto.PetDto;
import com.petapp.service.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gateway")
public class GatewayController {
    private static final Logger logger = LoggerFactory.getLogger(GatewayController.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ResponseHandler responseHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GatewayController(KafkaTemplate<String, String> kafkaTemplate, ResponseHandler responseHandler) {
        this.kafkaTemplate = kafkaTemplate;
        this.responseHandler = responseHandler;
    }

    @Operation(summary = "Get all owners", description = "Retrieves a list of all owners in the system.")
    @GetMapping("/owners")
    public ResponseEntity<List<OwnerDto>> getAllOwners() {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending get_all_owners command with correlationId: {}", correlationId);
            CommandMessage<String> message = new CommandMessage<>("get_all_owners", null, correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                throw new RuntimeException((String) responseMessage.getData());
            }
            List<OwnerDto> owners = objectMapper.convertValue(responseMessage.getData(), new TypeReference<List<OwnerDto>>() {});
            return ResponseEntity.ok(owners);
        } catch (Exception e) {
            logger.error("Error getting all owners: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get owner by ID", description = "Retrieves an owner by their ID, including the list of their pet IDs.")
    @GetMapping("/owners/{id}")
    public ResponseEntity<OwnerDto> getOwnerById(
            @Parameter(description = "ID of the owner to retrieve") @PathVariable Long id) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending get_owner_by_id command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<Long> message = new CommandMessage<>("get_owner_by_id", id, correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Owner not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            OwnerDto ownerDto = objectMapper.convertValue(responseMessage.getData(), OwnerDto.class);
            correlationId = UUID.randomUUID().toString();
            CommandMessage<Long> petMessage = new CommandMessage<>("get_pets_by_owner_id", id, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(petMessage));
            String petResponse = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> petResponseMessage = objectMapper.readValue(petResponse, CommandMessage.class);
            if ("success".equals(petResponseMessage.getCommand())) {
                List<Long> petIds = objectMapper.convertValue(petResponseMessage.getData(), new TypeReference<List<Long>>() {});
                ownerDto.setPetIds(petIds);
            }
            return ResponseEntity.ok(ownerDto);
        } catch (Exception e) {
            logger.error("Error getting owner by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Create a new owner", description = "Creates a new owner with the provided details.")
    @PostMapping("/owners")
    public ResponseEntity<OwnerDto> createOwner(
            @Parameter(description = "Details of the owner to create") @RequestBody OwnerDto ownerDto) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending create owner command with correlationId: {}", correlationId);
            CommandMessage<OwnerDto> message = new CommandMessage<>("create", ownerDto, correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                throw new RuntimeException((String) responseMessage.getData());
            }
            OwnerDto createdOwner = objectMapper.convertValue(responseMessage.getData(), OwnerDto.class);
            return ResponseEntity.ok(createdOwner);
        } catch (Exception e) {
            logger.error("Error creating owner: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Update an owner", description = "Updates the details of an existing owner by their ID.")
    @PutMapping("/owners/{id}")
    public ResponseEntity<OwnerDto> updateOwner(
            @Parameter(description = "ID of the owner to update") @PathVariable Long id,
            @Parameter(description = "Updated owner details") @RequestBody OwnerDto ownerDto) {
        try {
            ownerDto.setId(id);
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending update owner command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<OwnerDto> message = new CommandMessage<>("update", ownerDto, correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Owner not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            OwnerDto updatedOwner = objectMapper.convertValue(responseMessage.getData(), OwnerDto.class);
            return ResponseEntity.ok(updatedOwner);
        } catch (Exception e) {
            logger.error("Error updating owner with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Delete an owner", description = "Deletes an owner by their ID.")
    @DeleteMapping("/owners/{id}")
    public ResponseEntity<Void> deleteOwner(
            @Parameter(description = "ID of the owner to delete") @PathVariable Long id) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending delete owner command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<Long> message = new CommandMessage<>("delete", id, correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Owner not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting owner with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get all pets", description = "Retrieves a list of all pets in the system.")
    @GetMapping("/pets")
    public ResponseEntity<List<PetDto>> getAllPets() {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending get_all_pets command with correlationId: {}", correlationId);
            CommandMessage<String> message = new CommandMessage<>("get_all_pets", null, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                throw new RuntimeException((String) responseMessage.getData());
            }
            List<PetDto> pets = objectMapper.convertValue(responseMessage.getData(), new TypeReference<List<PetDto>>() {});
            return ResponseEntity.ok(pets);
        } catch (Exception e) {
            logger.error("Error getting all pets: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Get pet by ID", description = "Retrieves a pet by their ID.")
    @GetMapping("/pets/{id}")
    public ResponseEntity<PetDto> getPetById(
            @Parameter(description = "ID of the pet to retrieve") @PathVariable Long id) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending get_pet_by_id command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<Long> message = new CommandMessage<>("get_pet_by_id", id, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Pet not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            PetDto petDto = objectMapper.convertValue(responseMessage.getData(), PetDto.class);
            return ResponseEntity.ok(petDto);
        } catch (Exception e) {
            logger.error("Error getting pet by id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Create a new pet", description = "Creates a new pet with the provided details. Checks if the owner exists.")
    @PostMapping("/pets")
    public ResponseEntity<PetDto> createPet(
            @Parameter(description = "Details of the pet to create") @RequestBody PetDto petDto) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Checking owner existence for id {} with correlationId: {}", petDto.getOwnerId(), correlationId);
            CommandMessage<Long> ownerCheck = new CommandMessage<>("get_owner_by_id", petDto.getOwnerId(), correlationId);
            kafkaTemplate.send("owner-commands", objectMapper.writeValueAsString(ownerCheck));
            String ownerResponse = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> ownerResponseMessage = objectMapper.readValue(ownerResponse, CommandMessage.class);
            if ("error".equals(ownerResponseMessage.getCommand()) || ownerResponseMessage.getData() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            correlationId = UUID.randomUUID().toString();
            logger.info("Sending create pet command with correlationId: {}", correlationId);
            CommandMessage<PetDto> message = new CommandMessage<>("create", petDto, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                throw new RuntimeException((String) responseMessage.getData());
            }
            PetDto createdPet = objectMapper.convertValue(responseMessage.getData(), PetDto.class);
            return ResponseEntity.ok(createdPet);
        } catch (Exception e) {
            logger.error("Error creating pet: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Update a pet", description = "Updates the details of an existing pet by their ID.")
    @PutMapping("/pets/{id}")
    public ResponseEntity<PetDto> updatePet(
            @Parameter(description = "ID of the pet to update") @PathVariable Long id,
            @Parameter(description = "Updated pet details") @RequestBody PetDto petDto) {
        try {
            petDto.setId(id);
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending update pet command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<PetDto> message = new CommandMessage<>("update", petDto, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Pet not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            PetDto updatedPet = objectMapper.convertValue(responseMessage.getData(), PetDto.class);
            return ResponseEntity.ok(updatedPet);
        } catch (Exception e) {
            logger.error("Error updating pet with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Delete a pet", description = "Deletes a pet by their ID.")
    @DeleteMapping("/pets/{id}")
    public ResponseEntity<Void> deletePet(
            @Parameter(description = "ID of the pet to delete") @PathVariable Long id) {
        try {
            String correlationId = UUID.randomUUID().toString();
            logger.info("Sending delete pet command for id {} with correlationId: {}", id, correlationId);
            CommandMessage<Long> message = new CommandMessage<>("delete", id, correlationId);
            kafkaTemplate.send("pet-commands", objectMapper.writeValueAsString(message));
            String response = responseHandler.waitForResponse(correlationId);
            CommandMessage<?> responseMessage = objectMapper.readValue(response, CommandMessage.class);
            if ("error".equals(responseMessage.getCommand())) {
                if ("Pet not found".equals(responseMessage.getData())) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
                }
                throw new RuntimeException((String) responseMessage.getData());
            }
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error("Error deleting pet with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
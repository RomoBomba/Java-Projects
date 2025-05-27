package com.petapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.dto.CommandMessage;
import com.petapp.dto.PetDto;
import com.petapp.entities.Pet;
import com.petapp.mapper.PetMapper;
import com.petapp.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PetService {
    private static final Logger logger = LoggerFactory.getLogger(PetService.class);
    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PetService(PetRepository petRepository,
                      PetMapper petMapper,
                      KafkaTemplate<String, String> kafkaTemplate) {
        this.petRepository = petRepository;
        this.petMapper = petMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "pet-commands", groupId = "pet-group")
    @Transactional
    public void handleCommand(String message) throws JsonProcessingException {
        String correlationId = null;
        try {
            CommandMessage<?> commandMessage = objectMapper.readValue(message, CommandMessage.class);
            String command = commandMessage.getCommand();
            correlationId = commandMessage.getCorrelationId();

            switch (command) {
                case "create":
                    PetDto createDto = objectMapper.convertValue(commandMessage.getData(), PetDto.class);
                    Pet newPet = petMapper.toEntity(createDto);
                    Pet savedPet = petRepository.save(newPet);
                    CommandMessage<PetDto> createResponse = new CommandMessage<>("success", petMapper.toDto(savedPet), correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(createResponse));
                    break;
                case "update":
                    PetDto updateDto = objectMapper.convertValue(commandMessage.getData(), PetDto.class);
                    Pet existingPet = petRepository.findById(updateDto.getId())
                            .orElseThrow(() -> new RuntimeException("Pet not found"));
                    petMapper.updateEntity(existingPet, updateDto);
                    Pet updatedPet = petRepository.save(existingPet);
                    CommandMessage<PetDto> updateResponse = new CommandMessage<>("success", petMapper.toDto(updatedPet), correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(updateResponse));
                    break;
                case "delete":
                    Long id = objectMapper.convertValue(commandMessage.getData(), Long.class);
                    petRepository.deleteById(id);
                    CommandMessage<String> deleteResponse = new CommandMessage<>("success", "Pet deleted: " + id, correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(deleteResponse));
                    break;
                case "get_all_pets":
                    List<PetDto> pets = petRepository.findAll().stream()
                            .map(petMapper::toDto)
                            .collect(Collectors.toList());
                    CommandMessage<List<PetDto>> allResponse = new CommandMessage<>("success", pets, correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(allResponse));
                    break;
                case "get_pet_by_id":
                    Long petId = objectMapper.convertValue(commandMessage.getData(), Long.class);
                    PetDto petDto = petRepository.findById(petId)
                            .map(petMapper::toDto)
                            .orElseThrow(() -> new RuntimeException("Pet not found"));
                    CommandMessage<PetDto> petResponse = new CommandMessage<>("success", petDto, correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(petResponse));
                    break;
                case "get_pets_by_owner_id":
                    Long ownerId = objectMapper.convertValue(commandMessage.getData(), Long.class);
                    List<Long> petIds = petRepository.findAll().stream()
                            .filter(pet -> ownerId.equals(pet.getOwnerId()))
                            .map(Pet::getId)
                            .collect(Collectors.toList());
                    CommandMessage<List<Long>> ownerPetResponse = new CommandMessage<>("success", petIds, correlationId);
                    kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(ownerPetResponse));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + command);
            }
        } catch (Exception e) {
            logger.error("Error processing command: {}", e.getMessage(), e);
            CommandMessage<String> errorResponse = new CommandMessage<>("error", e.getMessage(), correlationId);
            kafkaTemplate.send("pet-response-topic", objectMapper.writeValueAsString(errorResponse));
        }
    }
}
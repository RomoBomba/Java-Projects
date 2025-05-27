package com.petapp.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.dto.CommandMessage;
import com.petapp.dto.OwnerDto;
import com.petapp.entities.Owner;
import com.petapp.mapper.OwnerMapper;
import com.petapp.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OwnerService {
    private static final Logger logger = LoggerFactory.getLogger(OwnerService.class);
    private final OwnerRepository ownerRepository;
    private final OwnerMapper ownerMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OwnerService(OwnerRepository ownerRepository,
                        OwnerMapper ownerMapper,
                        KafkaTemplate<String, String> kafkaTemplate) {
        this.ownerRepository = ownerRepository;
        this.ownerMapper = ownerMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "owner-commands", groupId = "owner-group")
    @Transactional
    public void handleCommand(String message) throws JsonProcessingException {
        String correlationId = null;
        try {
            CommandMessage<?> commandMessage = objectMapper.readValue(message, CommandMessage.class);
            String command = commandMessage.getCommand();
            correlationId = commandMessage.getCorrelationId();

            switch (command) {
                case "create":
                    OwnerDto createDto = objectMapper.convertValue(commandMessage.getData(), OwnerDto.class);
                    Owner createdOwner = ownerRepository.save(ownerMapper.toEntity(createDto));
                    CommandMessage<OwnerDto> createResponse = new CommandMessage<>("success", ownerMapper.toDto(createdOwner), correlationId);
                    kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(createResponse));
                    break;
                case "update":
                    OwnerDto updateDto = objectMapper.convertValue(commandMessage.getData(), OwnerDto.class);
                    Owner owner = ownerRepository.findById(updateDto.getId())
                            .orElseThrow(() -> new RuntimeException("Owner not found"));
                    ownerMapper.updateEntity(updateDto, owner);
                    Owner updatedOwner = ownerRepository.save(owner);
                    CommandMessage<OwnerDto> updateResponse = new CommandMessage<>("success", ownerMapper.toDto(updatedOwner), correlationId);
                    kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(updateResponse));
                    break;
                case "delete":
                    Long id = objectMapper.convertValue(commandMessage.getData(), Long.class);
                    ownerRepository.deleteById(id);
                    CommandMessage<String> deleteResponse = new CommandMessage<>("success", "Owner deleted: " + id, correlationId);
                    kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(deleteResponse));
                    break;
                case "get_all_owners":
                    List<OwnerDto> owners = ownerRepository.findAll().stream()
                            .map(ownerMapper::toDto)
                            .collect(Collectors.toList());
                    CommandMessage<List<OwnerDto>> allResponse = new CommandMessage<>("success", owners, correlationId);
                    kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(allResponse));
                    break;
                case "get_owner_by_id":
                    Long ownerId = objectMapper.convertValue(commandMessage.getData(), Long.class);
                    OwnerDto ownerDto = ownerRepository.findById(ownerId)
                            .map(ownerMapper::toDto)
                            .orElseThrow(() -> new RuntimeException("Owner not found"));
                    CommandMessage<OwnerDto> ownerResponse = new CommandMessage<>("success", ownerDto, correlationId);
                    kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(ownerResponse));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown command: " + command);
            }
        } catch (Exception e) {
            logger.error("Error processing command: {}", e.getMessage(), e);
            CommandMessage<String> errorResponse = new CommandMessage<>("error", e.getMessage(), correlationId);
            kafkaTemplate.send("owner-response-topic", objectMapper.writeValueAsString(errorResponse));
        }
    }
}
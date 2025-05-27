package com.petapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petapp.dto.CommandMessage;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class ResponseHandler {
    private final Map<String, CompletableFuture<String>> responseFutures = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = {"owner-response-topic", "pet-response-topic"})
    public void handleResponse(String message) throws Exception {
        CommandMessage<?> response = objectMapper.readValue(message, CommandMessage.class);
        String correlationId = response.getCorrelationId();
        CompletableFuture<String> future = responseFutures.get(correlationId);
        if (future != null) {
            future.complete(message);
        }
    }

    public String waitForResponse(String correlationId) throws Exception {
        CompletableFuture<String> future = new CompletableFuture<>();
        responseFutures.put(correlationId, future);
        try {
            return future.get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.REQUEST_TIMEOUT, "Service timeout");
        } finally {
            responseFutures.remove(correlationId);
        }
    }
}
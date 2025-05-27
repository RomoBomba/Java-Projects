package com.petapp.dto;

public class CommandMessage<T> {
    private String command;
    private T data;
    private String correlationId;

    public CommandMessage() {}

    public CommandMessage(String command, T data, String correlationId) {
        this.command = command;
        this.data = data;
        this.correlationId = correlationId;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}
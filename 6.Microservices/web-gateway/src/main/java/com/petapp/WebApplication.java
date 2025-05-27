package com.petapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}

// cd C:\kafka\kafka_2.13-3.8.1\bin/windows
// .\zookeeper-server-start.bat ..\..\config\zookeeper.properties (1 terminal)
// .\kafka-server-start.bat ..\..\config\server.properties (2 terminal)
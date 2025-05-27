package com.petapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic petCommands() {
        return TopicBuilder.name("pet-commands")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic petEvents() {
        return TopicBuilder.name("pet-events")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic petResponseTopic() {
        return TopicBuilder.name("pet-response-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
package com.petapp.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic userCommands() {
        return TopicBuilder.name("user-commands")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic ownerResponseTopic() {
        return TopicBuilder.name("owner-response-topic")
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
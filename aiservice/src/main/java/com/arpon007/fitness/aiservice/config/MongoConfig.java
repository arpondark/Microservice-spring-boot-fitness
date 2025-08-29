package com.arpon007.fitness.aiservice.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // Register custom deserializer for LocalDateTime arrays
        SimpleModule customModule = new SimpleModule();
        customModule.addDeserializer(LocalDateTime.class, new LocalDateTimeArrayDeserializer());

        mapper.registerModule(javaTimeModule);
        mapper.registerModule(customModule);
        return mapper;
    }
}

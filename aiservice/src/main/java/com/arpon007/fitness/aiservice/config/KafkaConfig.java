package com.arpon007.fitness.aiservice.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.arpon007.fitness.aiservice.model.Activity;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Configuration
//@EnableKafka
public class KafkaConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    private final ObjectMapper objectMapper;

    public KafkaConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public ConsumerFactory<String, Activity> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put("bootstrap.servers", bootstrapServers);
        props.put("group.id", "activity-processor-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        JsonDeserializer<Activity> deserializer = new JsonDeserializer<>(Activity.class, objectMapper);
        deserializer.setUseTypeHeaders(false);
        deserializer.configure(props, false);

        return new DefaultKafkaConsumerFactory<>(props,
            new org.apache.kafka.common.serialization.StringDeserializer(),
            deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Activity> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Activity> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}

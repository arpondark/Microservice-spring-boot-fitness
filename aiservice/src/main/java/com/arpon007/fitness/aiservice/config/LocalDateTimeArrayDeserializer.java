package com.arpon007.fitness.aiservice.config;

import java.io.IOException;
import java.time.LocalDateTime;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class LocalDateTimeArrayDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node.isArray() && node.size() >= 5) {
            int year = node.get(0).asInt();
            int month = node.get(1).asInt();
            int day = node.get(2).asInt();
            int hour = node.get(3).asInt();
            int minute = node.get(4).asInt();

            // Handle different array lengths
            if (node.size() >= 6) {
                int second = node.get(5).asInt();
                if (node.size() >= 7) {
                    // Ignore nanoseconds for LocalDateTime
                    return LocalDateTime.of(year, month, day, hour, minute, second);
                } else {
                    return LocalDateTime.of(year, month, day, hour, minute, second);
                }
            } else {
                return LocalDateTime.of(year, month, day, hour, minute);
            }
        }

        // Fallback to default deserialization
        return LocalDateTime.parse(node.asText());
    }
}

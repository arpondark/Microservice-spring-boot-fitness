package com.arpon007.fitness.ActivityService.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserValidationService {

    private final WebClient webClient;

    public boolean validateUser(String userId) {
        log.info("Calling user Service for  id {}", userId);

        try {
            return webClient.get()
                    .uri("http://user-service/api/users/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error validating user {}: {}", userId, e.getMessage());
        }
        return false;
    }
}

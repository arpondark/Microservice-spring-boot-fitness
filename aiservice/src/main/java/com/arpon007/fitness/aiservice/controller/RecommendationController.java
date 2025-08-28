package com.arpon007.fitness.aiservice.controller;

import com.arpon007.fitness.aiservice.model.Recommendation;
import com.arpon007.fitness.aiservice.service.RecommentationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommandations")
public class RecommendationController {
    private final RecommentationService recommendationService;

    @GetMapping("/user/{userId")
    public ResponseEntity<List<Recommendation>> getUerRecommendations(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUerRecommendations(userId));

    }

    @GetMapping("/activity/{activityId")
    public ResponseEntity<List<Recommendation>> getActivityRecommendation(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }

}

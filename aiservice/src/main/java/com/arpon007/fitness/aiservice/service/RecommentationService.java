package com.arpon007.fitness.aiservice.service;

import com.arpon007.fitness.aiservice.model.Recommendation;
import com.arpon007.fitness.aiservice.repo.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommentationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getUerRecommendations(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public List<Recommendation> getActivityRecommendation(String activityId) {
        List<Recommendation> recommendations = recommendationRepository.findByActivityId(activityId);
        if (recommendations.isEmpty()) {
            throw new RuntimeException("No recommendations found for activityId: " + activityId);
        }
        return recommendations;
    }
}

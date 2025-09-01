package com.arpon007.fitness.aiservice.service;

import com.arpon007.fitness.aiservice.model.Activity;
import com.arpon007.fitness.aiservice.model.Recommendation;
import com.arpon007.fitness.aiservice.repo.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListener {
    private final ActivityAIService activityAIService;
    private final RecommendationRepository recommendationRepository;

    //@KafkaListener(topics = "${kafka.topic.name}", groupId = "activity-processor-group")
    public void processActivity(Activity activity) {
        log.info("Received activity: {}", activity.getUserId()); //log userId of the activity
        Recommendation recommendation = activityAIService.generateRecommendation(activity);
        recommendationRepository.save(recommendation);

    }
}

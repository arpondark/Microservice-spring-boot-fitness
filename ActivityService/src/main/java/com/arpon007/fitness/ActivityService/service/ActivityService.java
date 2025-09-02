package com.arpon007.fitness.ActivityService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.arpon007.fitness.ActivityService.dto.ActivityRequest;
import com.arpon007.fitness.ActivityService.dto.ActivityResponse;
import com.arpon007.fitness.ActivityService.model.Activity;
import com.arpon007.fitness.ActivityService.repo.ActivityRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepo activityRepo;
    private final UserValidationService userValidationService;
    private final KafkaTemplate<String,Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse tractActivity(ActivityRequest request) {
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid User ID: " + request.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startTime(request.getStartTime())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepo.save(activity);
        try {
            kafkaTemplate.send(topicName, savedActivity.getUserId(), savedActivity);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return mapToResponse(savedActivity);
    }

    public List<ActivityResponse> getActivitiesByUser(String userId) {
        List<Activity> activities = activityRepo.findByUserId(userId);
        return activities.stream()
                .map(this::mapToResponse)
                .collect(java.util.stream.Collectors.toList());
    }

    private ActivityResponse mapToResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());
        return response;
    }
}

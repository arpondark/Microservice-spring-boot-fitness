package com.arpon007.fitness.ActivityService.service;

import com.arpon007.fitness.ActivityService.dto.ActivityRequest;
import com.arpon007.fitness.ActivityService.dto.ActivityResponse;
import com.arpon007.fitness.ActivityService.model.Activity;
import com.arpon007.fitness.ActivityService.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepo activityRepo;
    private final UserValidationService userValidationService;

    public ActivityResponse tractActivity(ActivityRequest request) {
        boolean isValidUser = userValidationService.validateUser(request.getUserId());
        if(!isValidUser)
        {
            throw new RuntimeException("Invalid User ID: " + request.getUserId());
        }
        Activity activity = Activity.builder().userId(request.getUserId()).type(request.getType()).duration(request.getDuration()).caloriesBurned(request.getCaloriesBurned()).startTime(request.getStartTime()).additionalMetrics(request.getAdditionalMetrics()).build();

        Activity savedActivity = activityRepo.save(activity);
        return mapToResponse(savedActivity);
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

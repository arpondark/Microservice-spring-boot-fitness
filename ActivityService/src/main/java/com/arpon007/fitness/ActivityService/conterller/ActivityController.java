package com.arpon007.fitness.ActivityService.conterller;
import com.arpon007.fitness.ActivityService.dto.ActivityRequest;
import com.arpon007.fitness.ActivityService.dto.ActivityResponse;
import com.arpon007.fitness.ActivityService.model.Activity;
import com.arpon007.fitness.ActivityService.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<ActivityResponse> tractActivity(@RequestBody ActivityRequest request) {
        return ResponseEntity.ok(activityService.tractActivity(request));

    }
}

package io.github.andz138.predictafit.activityservice.service;

import io.github.andz138.predictafit.activityservice.domain.Activity;
import io.github.andz138.predictafit.activityservice.dto.ActivityRequest;
import io.github.andz138.predictafit.activityservice.dto.ActivityResponse;
import io.github.andz138.predictafit.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserValidationService userValidationService;

    public ActivityResponse createActivity(String userId, ActivityRequest request) {
        userValidationService.assertUserExists(userId);

        Activity activity = toActivity(userId, request);
        Activity savedActivity = activityRepository.save(activity);
        return toResponse(savedActivity);
    }

    public List<ActivityResponse> getActivitiesForUser(String userId) {
        return activityRepository.findAllByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ActivityResponse getActivityById(String activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Activity not found: " + activityId)
                );

        return toResponse(activity);
    }

    private Activity toActivity(String userId, ActivityRequest request) {
        return Activity.builder()
                .userId(userId)
                .activityType(request.activityType())
                .durationMinutes(request.durationMinutes())
                .caloriesBurned(request.caloriesBurned())
                .startedAt(request.startedAt())
                .metrics(request.metrics())
                .build();
    }

    private ActivityResponse toResponse(Activity activity) {
        return new ActivityResponse(
                activity.getActivityId(),
                activity.getUserId(),
                activity.getActivityType(),
                activity.getDurationMinutes(),
                activity.getCaloriesBurned(),
                activity.getStartedAt(),
                activity.getMetrics(),
                activity.getCreatedAt(),
                activity.getUpdatedAt()
        );
    }
}
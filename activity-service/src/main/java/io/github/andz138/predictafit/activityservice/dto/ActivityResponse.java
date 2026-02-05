package io.github.andz138.predictafit.activityservice.dto;

import io.github.andz138.predictafit.activityservice.domain.ActivityType;

import java.time.LocalDateTime;
import java.util.Map;

public record ActivityResponse(
        String activityId,
        String userId,
        ActivityType activityType,
        Integer durationMinutes,
        Integer caloriesBurned,
        LocalDateTime startedAt,
        Map<String, Object> metrics,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
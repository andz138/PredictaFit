package io.github.andz138.predictafit.activityservice.messaging.event;

import java.time.LocalDateTime;

public record ActivityCreatedEvent(
        String activityId,
        String userId,
        String activityType,
        Integer durationMinutes,
        Integer caloriesBurned,
        LocalDateTime startedAt
) {}

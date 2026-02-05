package io.github.andz138.predictafit.activityservice.dto;

import io.github.andz138.predictafit.activityservice.domain.ActivityType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.Map;

public record ActivityRequest(
        @NotNull
        ActivityType activityType,

        @Positive
        Integer durationMinutes,

        @PositiveOrZero
        Integer caloriesBurned,

        @NotNull
        LocalDateTime startedAt,

        Map<String, Object> metrics
) {}
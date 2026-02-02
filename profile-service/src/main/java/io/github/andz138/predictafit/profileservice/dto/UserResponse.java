package io.github.andz138.predictafit.profileservice.dto;

import java.time.LocalDateTime;

public record UserResponse(
        String userId,
        String email,
        String firstName,
        String lastName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) { }
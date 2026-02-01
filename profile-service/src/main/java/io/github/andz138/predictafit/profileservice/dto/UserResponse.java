package io.github.andz138.predictafit.profileservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String userId;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

package io.github.andz138.predictafit.activityservice.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activities")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {

    @Id
    private String activityId;

    private String userId;

    private ActivityType activityType;

    private Integer durationMinutes;

    private Integer caloriesBurned;

    private LocalDateTime startedAt;

    @Field("metrics")
    private Map<String, Object> metrics;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

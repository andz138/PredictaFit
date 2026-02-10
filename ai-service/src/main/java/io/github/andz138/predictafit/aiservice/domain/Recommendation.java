package io.github.andz138.predictafit.aiservice.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendations")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    @Id
    private String recommendationId;
    private String activityId;
    private String userId;
    private String activityType;
    private String overallRecommendation;
    private List<String> improvementAreas;
    private List<String> suggestions;
    private List<String> safetyNotes;

    @CreatedDate
    private LocalDateTime createdAt;
}

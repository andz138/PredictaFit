package io.github.andz138.predictafit.aiservice.controller;

import io.github.andz138.predictafit.aiservice.domain.Recommendation;
import io.github.andz138.predictafit.aiservice.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsForUser(userId));
    }

    @GetMapping("/activities/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationForActivity(
            @PathVariable String activityId
    ) {
        return recommendationService.findRecommendationForActivity(activityId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }
}

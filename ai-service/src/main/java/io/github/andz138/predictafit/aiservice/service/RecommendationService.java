package io.github.andz138.predictafit.aiservice.service;

import io.github.andz138.predictafit.aiservice.domain.Recommendation;
import io.github.andz138.predictafit.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public List<Recommendation> getRecommendationsForUser(String userId) {
        return recommendationRepository.findByUserId(userId);
    }

    public Optional<Recommendation> findRecommendationForActivity(String activityId) {
        return recommendationRepository.findByActivityId(activityId);
    }
}

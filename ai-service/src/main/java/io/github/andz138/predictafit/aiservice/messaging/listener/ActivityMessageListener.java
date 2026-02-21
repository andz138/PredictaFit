package io.github.andz138.predictafit.aiservice.messaging.listener;

import io.github.andz138.predictafit.aiservice.domain.Recommendation;
import io.github.andz138.predictafit.aiservice.integration.gemini.GeminiRateLimitException;
import io.github.andz138.predictafit.aiservice.messaging.event.ActivityCreatedEvent;
import io.github.andz138.predictafit.aiservice.recommendation.service.ActivityAIService;
import io.github.andz138.predictafit.aiservice.repository.RecommendationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;
    private final RecommendationRepository recommendationRepository;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivity(ActivityCreatedEvent event) {

        log.info("Processing activity for AI analysis. activityId={}", event.activityId());

        try {
            Recommendation recommendation = activityAIService.generateRecommendation(event);

            recommendationRepository.save(recommendation);

            log.info("Recommendation persisted successfully. activityId={}", event.activityId());

        } catch (GeminiRateLimitException ex) {

            log.warn("Rate limit encountered. Requeueing message. activityId={}", event.activityId());
            throw ex; // Let Rabbit retry

        } catch (Exception ex) {

            log.error("AI processing failed. activityId={}", event.activityId(), ex);
            throw ex;
        }
    }

    private String preview(String text, int maxLength) {
        if (text == null) return "(null)";
        String trimmed = text.trim();
        return trimmed.length() <= maxLength
                ? trimmed
                : trimmed.substring(0, maxLength) + "...";
    }
}

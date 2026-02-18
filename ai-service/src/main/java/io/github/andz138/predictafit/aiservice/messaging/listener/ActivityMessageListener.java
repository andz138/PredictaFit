package io.github.andz138.predictafit.aiservice.messaging.listener;

import io.github.andz138.predictafit.aiservice.integration.gemini.GeminiRateLimitException;
import io.github.andz138.predictafit.aiservice.messaging.event.ActivityCreatedEvent;
import io.github.andz138.predictafit.aiservice.recommendation.service.ActivityAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityMessageListener {

    private final ActivityAIService activityAIService;

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void processActivity(ActivityCreatedEvent event) {

        log.info("Received ActivityCreatedEvent. activityId={}, userId={}, type={}, durationMinutes={}",
                event.activityId(),
                event.userId(),
                event.activityType(),
                event.durationMinutes()
        );

        try {

            String recommendation = activityAIService.generateRecommendation(event);

            log.info("Recommendation generated. activityId={}, preview={}",
                    event.activityId(),
                    preview(recommendation, 200)
            );

        } catch (GeminiRateLimitException e) {

            log.warn("Gemini rate limit hit. Will retry. activityId={}", event.activityId());

            throw e; // Let Rabbit requeue

        } catch (Exception e) {

            log.error("Unexpected AI failure. activityId={}", event.activityId(), e);

            throw e; // Also requeue unless you configure otherwise
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

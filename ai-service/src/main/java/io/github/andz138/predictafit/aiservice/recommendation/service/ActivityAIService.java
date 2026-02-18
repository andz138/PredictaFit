package io.github.andz138.predictafit.aiservice.recommendation.service;

import io.github.andz138.predictafit.aiservice.integration.gemini.GeminiService;
import io.github.andz138.predictafit.aiservice.messaging.event.ActivityCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;

    public String generateRecommendation(ActivityCreatedEvent event) {

        String prompt = buildPrompt(event);

        // Calls Gemini via your integration layer
        String aiResponse = geminiService.generateRecommendation(prompt);

        // Log safely: keep it short and avoid dumping large responses
        log.info("AI recommendation generated. activityId={}, preview={}",
                event.activityId(),
                preview(aiResponse, 200)
        );

        return aiResponse;
    }

    private String buildPrompt(ActivityCreatedEvent event) {

        return """
                You are a certified fitness coach.

                Based on the activity below, generate:
                1) A short encouraging summary
                2) One actionable improvement suggestion
                3) One recovery or health tip

                Constraints:
                - Keep under 120 words
                - Do not make medical diagnoses
                - Use a friendly, motivational tone
                - Avoid emojis

                Activity Details:
                activityId: %s
                userId: %s
                type: %s
                durationMinutes: %s
                caloriesBurned: %s
                startedAt: %s
                """.formatted(
                event.activityId(),
                event.userId(),
                event.activityType(),
                event.durationMinutes(),
                event.caloriesBurned(),
                event.startedAt()
        );
    }

    private String preview(String text, int maxLength) {
        if (text == null) return "(null)";
        String trimmed = text.trim();
        return trimmed.length() <= maxLength
                ? trimmed
                : trimmed.substring(0, maxLength) + "...";
    }
}
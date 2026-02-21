package io.github.andz138.predictafit.aiservice.recommendation.service;

import io.github.andz138.predictafit.aiservice.domain.Recommendation;
import io.github.andz138.predictafit.aiservice.integration.gemini.GeminiService;
import io.github.andz138.predictafit.aiservice.messaging.event.ActivityCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Application/use-case service that orchestrates: ActivityCreatedEvent -> AI recommendation text.
 *
 * Responsibilities:
 * - Convert the incoming activity event into a prompt (string)
 * - Delegate the external AI call to GeminiService
 * - Log and return the generated recommendation
 *
 * This class should not contain low-level HTTP details. That belongs in GeminiService.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ActivityAIService {

    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    /**
     * Generates an AI recommendation for the given activity event.
     *
     * Flow:
     * 1) Build a prompt string from the event (domain data -> natural language instructions)
     * 2) Send the prompt to GeminiService (integration layer)
     * 3) Log a preview for debugging
     * 4) Return the full AI response text
     *
     * @param event ActivityCreatedEvent DTO containing activity details
     * @return AI-generated recommendation text
     */
    public Recommendation generateRecommendation(ActivityCreatedEvent event) {

        String prompt = buildPrompt(event);

        String rawResponse = geminiService.generateRecommendation(prompt);

        log.debug("AI response received. activityId={}", event.activityId());

        return processAiResponse(event, rawResponse);
    }


    private Recommendation processAiResponse(ActivityCreatedEvent event, String rawResponse) {

        if (rawResponse == null || rawResponse.isBlank()) {
            log.warn("Empty Gemini response. activityId={}", event.activityId());
            return createDefaultRecommendation(event);
        }

        try {
            // Extract JSON from fenced markdown if present
            String jsonPayload = sanitizeMarkdown(rawResponse);

            JsonNode rootJson = objectMapper.readTree(jsonPayload);

            return mapToRecommendation(event, rootJson);

        } catch (Exception ex) {
            log.error("Failed parsing Gemini response. activityId={}", event.activityId(), ex);
            return createDefaultRecommendation(event);
        }
    }


    private Recommendation mapToRecommendation(ActivityCreatedEvent event, JsonNode json) {
        JsonNode analysisNode = json.path("analysis");

        StringBuilder fullAnalysis = new StringBuilder();
        appendAnalysis(fullAnalysis, analysisNode, "overall", "Overall");
        appendAnalysis(fullAnalysis, analysisNode, "pace", "Pace");
        appendAnalysis(fullAnalysis, analysisNode, "heartRate", "Heart Rate");
        appendAnalysis(fullAnalysis, analysisNode, "caloriesBurned", "Calories");

        List<String> improvements = extractImprovements(json.path("improvements"));
        List<String> suggestions = extractSuggestions(json.path("suggestions"));
        List<String> safety = extractSafety(json.path("safety"));

        return buildRecommendation(
                event,
                fullAnalysis.toString().trim(),
                improvements,
                suggestions,
                safety
        );
    }

    private Recommendation buildRecommendation(
            ActivityCreatedEvent event,
            String overallRecommendation,
            List<String> improvementAreas,
            List<String> suggestions,
            List<String> safetyNotes
    ) {
        return Recommendation.builder()
                .activityId(event.activityId())
                .userId(event.userId())
                .activityType(event.activityType())
                .overallRecommendation(overallRecommendation)
                .improvementAreas(improvementAreas)
                .suggestions(suggestions)
                .safetyNotes(safetyNotes)
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractImprovements(JsonNode node) {
        if (!node.isArray()) return List.of("Maintain consistent effort and progression");

        List<String> results = new ArrayList<>();
        node.forEach(item -> results.add(
                "%s: %s".formatted(
                        item.path("area").asText(),
                        item.path("recommendation").asText()
                )
        ));

        return results.isEmpty() ? List.of("Focus on one small improvement next session.") : results;
    }

    private List<String> extractSuggestions(JsonNode node) {
        if (!node.isArray()) return List.of("Incorporate balanced cross-training sessions.");

        List<String> results = new ArrayList<>();
        node.forEach(item -> results.add(
                "%s: %s".formatted(
                        item.path("workout").asText(),
                        item.path("description").asText()
                )
        ));

        return results.isEmpty() ? List.of("Consider interval-based training next session.") : results;
    }

    private List<String> extractSafety(JsonNode node) {
        if (!node.isArray()) return List.of("Stay hydrated and warm up properly.");

        List<String> results = new ArrayList<>();
        node.forEach(item -> results.add(item.asText()));

        return results.isEmpty() ? List.of("Monitor fatigue levels and avoid overtraining.") : results;
    }


    private void appendAnalysis(StringBuilder sb, JsonNode node, String key, String label) {
        if (!node.path(key).isMissingNode()) {
            sb.append(label)
                .append(": ")
                .append(node.path(key).asText())
                .append("\n\n");
        }
    }

    private String sanitizeMarkdown(String text) {
        if (text == null) return "";

        return text
                .replaceAll("```json\\n","")
                .replaceAll("\\n```", "")
                .trim();
    }


    private Recommendation createDefaultRecommendation(ActivityCreatedEvent event) {
        return Recommendation.builder()
                .activityId(event.activityId())
                .userId(event.userId())
                .activityType(event.activityType())
                .overallRecommendation("Analysis temporarily unavailable.")
                .improvementAreas(List.of("Continue with current ."))
                .suggestions(List.of("Review pacing strategy for your next workout."))
                .safetyNotes(List.of(
                        "Warm up before exercise",
                        "Stay hydrated",
                        "Listen to your body"
                ))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private String buildPrompt(ActivityCreatedEvent event) {

        return """
        You are an experienced fitness performance analyst.
    
        Evaluate the activity below and generate a structured performance report.
        Your response MUST be valid JSON and MUST strictly follow the schema provided.
        Do not include markdown, commentary, or extra fields.
    
        Required JSON structure:
    
        {
          "analysis": {
            "overall": "Overall performance evaluation",
            "pace": "Pacing analysis",
            "heartRate": "Heart rate interpretation",
            "caloriesBurned": "Calorie efficiency analysis"
          },
          "improvements": [
            {
              "area": "Performance area",
              "recommendation": "Specific improvement guidance"
            }
          ],
          "suggestions": [
            {
              "workout": "Suggested workout name",
              "description": "Detailed explanation of the workout"
            }
          ],
          "safety": [
            "Safety recommendation 1",
            "Safety recommendation 2"
          ]
        }
    
        Activity Context:
        - Activity ID: %s
        - User ID: %s
        - Activity Type: %s
        - Duration (minutes): %s
        - Calories Burned: %s
        - Started At: %s
    
        Requirements:
        - Provide actionable and practical insights
        - Keep explanations concise but meaningful
        - Focus on performance optimization and recovery
        - Return ONLY valid JSON
        """.formatted(
                    event.activityId(),
                    event.userId(),
                    event.activityType(),
                    event.durationMinutes(),
                    event.caloriesBurned(),
                    event.startedAt()
        );
    }
}
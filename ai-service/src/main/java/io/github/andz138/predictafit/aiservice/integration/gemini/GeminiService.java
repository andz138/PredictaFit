package io.github.andz138.predictafit.aiservice.integration.gemini;

import io.github.andz138.predictafit.aiservice.integration.gemini.dto.GeminiGenerateRequest;
import io.github.andz138.predictafit.aiservice.integration.gemini.dto.GeminiGenerateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;

/**
 * Integration service (infrastructure layer) for calling the Gemini API.
 *
 * Responsibilities:
 * - Wrap a prompt string into the request DTO Gemini expects
 * - Send the HTTP request using WebClient
 * - Map error status codes into domain-specific exceptions
 * - Deserialize the response DTO and return the generated text
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {
    // Hard limit on how long we allow a Gemini call to take before failing.
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);
    private final WebClient geminiWebClient;
    // Holds configuration like the Gemini endpoint URL.
    private final GeminiProperties geminiProperties;

    /**
     * Sends the given prompt to Gemini and returns the model's generated text.
     *
     * @param prompt Prompt text to send to Gemini
     * @return Generated recommendation text (first candidate / first part)
     */
    public String generateRecommendation(String prompt) {
        GeminiGenerateRequest request = new GeminiGenerateRequest(
                List.of(new GeminiGenerateRequest.Content(
                        List.of(new GeminiGenerateRequest.Part(prompt))
                ))
        );

        GeminiGenerateResponse response = geminiWebClient
                .post()
                .uri(geminiProperties.getUrl())
                .bodyValue(request)
                .retrieve()
                .onStatus(
                        status -> status.value() == 429,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(
                                        new GeminiRateLimitException(
                                                "Gemini rate limit hit (429)" +
                                                        (body.isBlank() ? "" : ": " + body)
                                        )
                                ))
                )
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> Mono.error(
                                        new GeminiApiException(
                                                "Gemini returned " + clientResponse.statusCode() +
                                                        (body.isBlank() ? "" : ": " + body)
                                        )
                                ))
                )
                .bodyToMono(GeminiGenerateResponse.class)
                .timeout(REQUEST_TIMEOUT)
                .block();

        if (response == null || response.firstText() == null) {
            throw new GeminiApiException("Gemini returned empty response");
        }

        return response.firstText();
    }
}

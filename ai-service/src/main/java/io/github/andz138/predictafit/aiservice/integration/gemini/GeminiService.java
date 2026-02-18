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

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiService {

    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(15);

    private final WebClient geminiWebClient;
    private final GeminiProperties geminiProperties;

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

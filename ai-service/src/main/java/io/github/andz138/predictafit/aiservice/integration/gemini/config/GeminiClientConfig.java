package io.github.andz138.predictafit.aiservice.integration.gemini.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class GeminiClientConfig {
    @Bean
    public WebClient geminiWebClient(@Value("${gemini.api.key}") String apiKey) {
        return WebClient.builder()
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("X-goog-api-key", apiKey)
                .build();
    }
}

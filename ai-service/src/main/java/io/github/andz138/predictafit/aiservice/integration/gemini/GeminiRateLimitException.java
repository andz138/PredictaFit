package io.github.andz138.predictafit.aiservice.integration.gemini;

public class GeminiRateLimitException extends RuntimeException {
    public GeminiRateLimitException(String message) {
        super(message);
    }
}

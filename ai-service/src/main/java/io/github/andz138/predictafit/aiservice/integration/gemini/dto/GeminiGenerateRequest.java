package io.github.andz138.predictafit.aiservice.integration.gemini.dto;

import java.util.List;

public record GeminiGenerateRequest(List<Content> contents) {
    public record Content(List<Part> parts) {}
    public record Part(String text) {}
}

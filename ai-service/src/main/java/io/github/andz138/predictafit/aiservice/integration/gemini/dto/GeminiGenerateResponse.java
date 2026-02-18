package io.github.andz138.predictafit.aiservice.integration.gemini.dto;

import java.util.List;

public record GeminiGenerateResponse(List<Candidate> candidates) {
    public record Candidate(Content content) {}
    public record Content(List<Part> parts) {}
    public record Part(String text) {}

    public String firstText() {
        if (candidates == null || candidates.isEmpty()) return null;

        Candidate c = candidates.get(0);

        if (c == null || c.content() == null || c.content.parts() == null || c.content().parts().isEmpty()) return null;

        return c.content().parts().get(0).text();
    }

}

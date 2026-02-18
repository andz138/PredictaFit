package io.github.andz138.predictafit.aiservice.integration.gemini;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "gemini.api")
public class GeminiProperties {
    private String url;
}

package io.github.andz138.predictafit.activityservice.service;

import io.github.andz138.predictafit.activityservice.exception.UserValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UserValidationService {

    private final WebClient profileServiceWebClient;

    public void assertUserExists(String userId) {
        Boolean exists = profileServiceWebClient.get()
                .uri("/api/users/{userId}/exists", userId)
                .retrieve()
                .onStatus(
                        HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new UserValidationException(
                                        "User validation failed for userId=%s (client error)".formatted(userId)
                                ))
                )
                .onStatus(
                        HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class)
                                .map(body -> new UserValidationException(
                                        "Profile-Service unavailable while validating userId=%s".formatted(userId)
                                ))
                )
                .bodyToMono(Boolean.class)
                .block();

        if (exists == null || !exists) {
            throw new UserValidationException(
                    "User does not exist or validation returned empty response (userId=%s)".formatted(userId)
            );
        }
    }
}

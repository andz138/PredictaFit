package io.github.andz138.predictafit.apigateway.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final WebClient profileWebClient;

    public Mono<Boolean> userExistsByKeycloakId(String keycloakId) {
        return profileWebClient.get()
                .uri("/api/users/{keycloakId}/exists", keycloakId)
                .retrieve()
                .bodyToMono(Boolean.class)
                .defaultIfEmpty(false);
    }

    public Mono<Void> registerUser(RegisterRequest request) {
        return profileWebClient.post()
                .uri("/api/users")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

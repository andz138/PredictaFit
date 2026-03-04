package io.github.andz138.predictafit.apigateway.security;

import io.github.andz138.predictafit.apigateway.user.RegisterRequest;
import io.github.andz138.predictafit.apigateway.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        return ReactiveSecurityContextHolder.getContext()
                .map(ctx -> (Jwt) ctx.getAuthentication().getPrincipal())
                .flatMap(jwt -> {

                    String keycloakId = jwt.getSubject(); // sub claim
                    String email = jwt.getClaimAsString("email");
                    String firstName = jwt.getClaimAsString("given_name");
                    String lastName = jwt.getClaimAsString("family_name");

                    RegisterRequest request = new RegisterRequest(
                            email,
                            keycloakId,
                            firstName,
                            lastName
                    );

                    return userService.userExistsByKeycloakId(keycloakId)
                            .flatMap(exists -> {
                                if (!exists) {
                                    log.info("[UserSync] Keycloak user {} not found in Profile-Service. Creating domain user.", keycloakId);
                                    return userService.registerUser(request);
                                }
                                log.debug("[UserSync] User {} already synchronized.", keycloakId);
                                return Mono.empty();
                            })
                            .then(Mono.defer(() -> {
                                ServerHttpRequest mutated = exchange.getRequest()
                                        .mutate()
                                        .header("X-User-ID", keycloakId)
                                        .build();

                                return chain.filter(exchange.mutate().request(mutated).build());
                            }));
                })
                .switchIfEmpty(chain.filter(exchange));
    }
}

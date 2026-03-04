package io.github.andz138.predictafit.apigateway.user;

public record RegisterRequest(
        String email,
        String keycloakId,
        String firstName,
        String lastName
) { }

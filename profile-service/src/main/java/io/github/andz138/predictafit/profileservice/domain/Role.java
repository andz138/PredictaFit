package io.github.andz138.predictafit.profileservice.domain;

import java.util.Locale;

public enum Role {
    USER,
    ADMIN;

    public static Role from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("UserRole is required");
        }
        return Role.valueOf(value.trim().toUpperCase(Locale.ROOT));
    }
}

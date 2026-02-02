package io.github.andz138.predictafit.profileservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8)
        String plainPassword,

        @Size(max = 50)
        String firstName,

        @Size(max = 50)
        String lastName
) { }

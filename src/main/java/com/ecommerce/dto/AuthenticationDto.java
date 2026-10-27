package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDto(
        @NotBlank(message = "L'Email  est obligatoire")
        String username,
        @NotBlank(message = "Le mot de passe  est obligatoire")
        String password
) {
}

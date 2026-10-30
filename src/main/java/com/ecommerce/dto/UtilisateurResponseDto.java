package com.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UtilisateurResponseDto(
        @Schema(
                description = "Identifiant unique du produit",
                example = "15"
        )
        int id,
        @Schema(
                description = "Nom de l'utilisateur",
                example = "kamdem"

        )
        String nom,
        @Schema(
                description = "Prenom de l'utilisateur",
                example = "romaric"

        )
        String prenom,
        @Schema(
                description = "Adresse Email de l'utilisateur",
                example = "romaric@gmail.com"

        )
        String email


) {
}

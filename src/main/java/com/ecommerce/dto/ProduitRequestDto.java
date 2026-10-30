package com.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProduitRequestDto(
        @Schema(
                description = "Nom du produit",
                example = "Ordinateur HP",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "le nom du produit est obligatoire")
        String nomProduit,
        @Schema(
                description = "Prix du produit en FCFA",
                example = "450000.00",
                minimum = "0",
                exclusiveMinimum = true,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @Positive(message = "Le prix doit être supérieur à zéro")
        @NotNull(message = "le prix est obligatoire")
        @Digits(integer = 10,fraction = 2, message = "Le nombre doit contenir au maximum 10 chiffres avant et 2 après la virgule.")
        BigDecimal prix,
        @Schema(
                description = "Quantité disponible en stock",
                example = "12",
                minimum = "0",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @PositiveOrZero(message = "Le stock ne peut pas être négatif")
        @NotNull(message = "la quantite est obligatoire")
        long stock,
        @Schema(
                description = "Identifiant UUID de la catégorie à laquelle appartient le produit",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "la categories du produit est obligatoire")
        String codeCategorie
) {
}

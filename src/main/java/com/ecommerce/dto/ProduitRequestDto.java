package com.ecommerce.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ProduitRequestDto(
        @NotBlank(message = "le nom du produit est obligatoire")
        String nomProduit,
        @NotNull(message = "le prix est obligatoire")
        @Digits(integer = 10,fraction = 2)
        BigDecimal prix,
        @NotNull(message = "la quantite est obligatoire")
        long stock,
        @NotBlank(message = "la categories du produit est obligatoire")
        String codeCategorie
) {
}

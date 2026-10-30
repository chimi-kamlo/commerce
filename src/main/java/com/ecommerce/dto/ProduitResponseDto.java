package com.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProduitResponseDto(
        @Schema(
                description = "Identifiant unique du produit",
                example = "15"
        )
        int id,
        @Schema(
                description = "Nom du produit",
                example = "Ordinateur HP"
        )
        String nomProduit,
        @Schema(
                description = "Prix du produit en FCFA",
                example = "450000.00"
        )
        BigDecimal prix,
        @Schema(
                description = "Quantité disponible en stock",
                example = "50"
        )
        long stock,
        @Schema(
                description = "image du produit",
                example = "image.png"
        )
        String image_url,
        @Schema(
                description = "catégorie du produit",
                example = "{'id':'1','libele':'Habits','codeCategorie':'2d76fa80-5836-499a-8518-dab9057f35d4'}"
        )
        CategorieResponseDto categorie

) {
}

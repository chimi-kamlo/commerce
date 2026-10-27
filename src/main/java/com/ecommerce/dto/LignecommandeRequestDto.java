package com.ecommerce.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LignecommandeRequestDto(
        @Min(value = 1, message = "La quantité minimale est 1")
        @NotNull(message = "La quantité est obligatoire")
        int quantite,
        @NotNull(message = "L'identifiant du produit est obligatoire")
        int produit_id
) {

}

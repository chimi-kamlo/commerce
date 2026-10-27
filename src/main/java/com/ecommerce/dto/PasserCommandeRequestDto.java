package com.ecommerce.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record PasserCommandeRequestDto(
        @NotEmpty(message = "Vous n'avez rien sélectionner, votre panier Le panier doit contenir au moins un produit! ")
        Set<LignecommandeRequestDto> lignes
) {
}

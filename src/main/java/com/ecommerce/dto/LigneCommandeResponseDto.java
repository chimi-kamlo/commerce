package com.ecommerce.dto;

import java.math.BigDecimal;
import java.util.Map;

public record LigneCommandeResponseDto(
        int id,
        int quantite,
        BigDecimal prixUnitaire,
        BigDecimal montant,
      Map<String,?> produit
        ) {
}

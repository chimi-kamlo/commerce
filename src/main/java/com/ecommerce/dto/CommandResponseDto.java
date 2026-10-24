package com.ecommerce.dto;

import com.ecommerce.entities.StatutCommande;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record CommandResponseDto(
        int id,
        String reference,
        LocalDateTime dateCommande,
        LocalDateTime dateModification,
        StatutCommande statut,
         BigDecimal montant,
        List<LigneCommandeResponseDto> ligneCommande

) {
}

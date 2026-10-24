package com.ecommerce.dto;

import com.ecommerce.entities.StatutCommande;
import com.ecommerce.entities.Utilisateur;

import java.time.LocalDateTime;

public record CommandeRequestDto(

        String reference,
        LocalDateTime dateCommande



) {
}

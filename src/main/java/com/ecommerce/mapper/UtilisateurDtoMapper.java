package com.ecommerce.mapper;

import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.function.Function;
@Component
public class UtilisateurDtoMapper implements Function<Utilisateur,UtilisateurResponseDto> {
    @Override
    public UtilisateurResponseDto apply(Utilisateur utilisateur) {
        return new UtilisateurResponseDto(
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getPrenom(),
                utilisateur.getEmail()
        );
    }
}

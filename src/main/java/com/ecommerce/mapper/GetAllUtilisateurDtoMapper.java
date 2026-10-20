package com.ecommerce.mapper;

import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.Utilisateur;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
@Component
public class GetAllUtilisateurDtoMapper implements Function<List<Utilisateur>,List<UtilisateurResponseDto>> {
    @Override
    public List<UtilisateurResponseDto> apply(List<Utilisateur> utilisateurs) {

        List<UtilisateurResponseDto> AllUtilisateur = utilisateurs.stream().map((user)->

                new UtilisateurResponseDto(user.getId(), user.getNom(), user.getPrenom(), user.getEmail())).toList();
        return AllUtilisateur;
    }
}

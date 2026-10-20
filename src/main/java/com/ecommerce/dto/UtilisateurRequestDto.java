package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UtilisateurRequestDto(
        @NotBlank(message = "nom obligatoire")
        String nom,
        @NotBlank(message = "prenom obligatoire")
        String prenom,
        @NotBlank(message = "nom obligatoire")
        @Email(message = "votre email n'est pas conforme")
        String email,
        @NotBlank(message = "email obligatoire")
        @Size(min = 8 , message ="votre mot de passe doit avoir au moins 8 characters" )
        String motDePasse
) {
}

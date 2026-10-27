package com.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9\\s]).{1,8}$",
                message = "Le mot de passe doit contenir au maximum 8 caractères, un chiffre, une majuscule, une minuscule et un caractère spécial."
        )
        String motDePasse
) {
}

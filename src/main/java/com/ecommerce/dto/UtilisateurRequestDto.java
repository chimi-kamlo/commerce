package com.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UtilisateurRequestDto(
        @Schema(
                description = "Nom de l'utilisateur",
                example = "kamdem",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "nom obligatoire")
        String nom,
        @Schema(
                description = "Prenom de l'utilisateur",
                example = "romaric",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "prenom obligatoire")
        String prenom,
        @Schema(
                description = "Adresse Email de l'utilisateur",
                example = "romaric@gmail.com",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "email obligatoire")
        @Email(message = "votre email n'est pas conforme")
        String email,
        @Schema(
                description = "Mot de passe de l'utilisateur",
                example = "Chimi@1$",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "email obligatoire")
        @Pattern(
                regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9\\s]).{1,8}$",
                message = "Le mot de passe doit contenir au maximum 8 caractères, un chiffre, une majuscule, une minuscule et un caractère spécial."
        )
        String motDePasse
) {
}

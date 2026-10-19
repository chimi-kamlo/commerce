package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

public record CategorieRequestDto (
        @NotBlank(message = "le libelé est obligatoire")
        String libele
){
}

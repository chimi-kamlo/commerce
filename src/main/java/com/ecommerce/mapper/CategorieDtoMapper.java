package com.ecommerce.mapper;

import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.entities.Categorie;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class CategorieDtoMapper implements Function<Categorie , CategorieResponseDto> {

    @Override
    public CategorieResponseDto apply(Categorie categorie) {
        return new CategorieResponseDto(
                categorie.getId(),
                categorie.getLibele(),
                categorie.getCodeCategorie()
        );
    }
}

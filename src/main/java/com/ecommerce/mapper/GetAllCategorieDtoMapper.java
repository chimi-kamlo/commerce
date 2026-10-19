package com.ecommerce.mapper;

import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.entities.Categorie;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GetAllCategorieDtoMapper implements Function<List<Categorie>,List<CategorieResponseDto>> {
    @Override
    public List<CategorieResponseDto> apply(List<Categorie> categories) {
 List<CategorieResponseDto> listeCategorie = categories.stream().map((categorie -> {
     return new CategorieResponseDto(categorie.getId(),categorie.getLibele(),categorie.getCodeCategorie());
 })).toList();
        return listeCategorie;
    }
}

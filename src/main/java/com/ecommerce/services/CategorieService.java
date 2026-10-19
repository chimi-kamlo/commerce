package com.ecommerce.services;

import com.ecommerce.dto.CategorieRequestDto;
import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.mapper.CategorieDtoMapper;
import com.ecommerce.mapper.GetAllCategorieDtoMapper;
import com.ecommerce.repositories.CategorieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategorieService {
    private final CategorieRepository categorieRepository;
    private final CategorieDtoMapper categorieDtoMapper;
    private final GetAllCategorieDtoMapper getAllCategorieDtoMapper;

    public CategorieService(CategorieRepository categorieRepository, CategorieDtoMapper categorieDtoMapper, GetAllCategorieDtoMapper getAllCategorieDtoMapper) {
        this.categorieRepository = categorieRepository;
        this.categorieDtoMapper = categorieDtoMapper;
        this.getAllCategorieDtoMapper = getAllCategorieDtoMapper;
    }


    public CategorieResponseDto createCategorie(CategorieRequestDto categorieRequestDto){
         Categorie categorie = new Categorie(categorieRequestDto.libele(), UUID.randomUUID().toString());
        Categorie categorieSauvegardee = this.categorieRepository.save(categorie);
        return categorieDtoMapper.apply(categorieSauvegardee);
    }

    public List<CategorieResponseDto>getAllCategorie(){
      List<Categorie> categories = this.categorieRepository.findAll();
      return getAllCategorieDtoMapper.apply(categories);
    }

}

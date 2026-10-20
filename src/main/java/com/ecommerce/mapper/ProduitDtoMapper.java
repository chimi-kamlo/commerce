package com.ecommerce.mapper;

import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.entities.Produit;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class ProduitDtoMapper implements Function<Produit, ProduitResponseDto> {


    @Override
    public ProduitResponseDto apply(Produit produit) {
       CategorieResponseDto categorie =null;
        if(produit.getCategorie()!=null){
            categorie=new CategorieResponseDto(produit.getCategorie().getId(),
                    produit.getCategorie().getLibele(),
                    produit.getCategorie().getCodeCategorie());
        }
        return new ProduitResponseDto(produit.getId(),produit.getNomProduit(),produit.getPrix(),produit.getStock(),produit.getImageProduit(),categorie);
    }
}

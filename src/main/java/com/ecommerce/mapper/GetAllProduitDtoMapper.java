package com.ecommerce.mapper;

import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.entities.Produit;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
@Component
public class GetAllProduitDtoMapper implements Function<List<Produit>,List<ProduitResponseDto>> {
    @Override
    public List<ProduitResponseDto> apply(List<Produit> produits) {

        List<ProduitResponseDto> produitResponseDtos = produits.stream().map((produit)->{
            CategorieResponseDto categorie=null;
            if(produit.getCategorie()!=null){
             categorie =new CategorieResponseDto(produit.getCategorie().getId(),
                        produit.getCategorie().getLibele(),
                        produit.getCategorie().getCodeCategorie());
            }
            return new ProduitResponseDto(produit.getId(),produit.getNomProduit(),produit.getPrix(),produit.getStock(),produit.getImageProduit(),categorie);
        }).toList();
        return produitResponseDtos;
    }
}

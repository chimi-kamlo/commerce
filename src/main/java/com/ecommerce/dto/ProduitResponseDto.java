package com.ecommerce.dto;

import java.math.BigDecimal;

public record ProduitResponseDto(int id,String nomProduit, BigDecimal prix, long stock,String image_url,CategorieResponseDto categorie) {
}

package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CategorieRequestDto;
import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.services.CategorieService;
import com.ecommerce.utils.ApiResponseRefactor;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "categories")
public class CategorieController {
    private  final CategorieService categorieService;
    private final ApiResponseRefactor apiResponseRefactor;

 // CREATION D'UNE CATEGORIE
    public CategorieController(CategorieService categorieService, ApiResponseRefactor apiResponseRefactor) {
        this.categorieService = categorieService;
        this.apiResponseRefactor = apiResponseRefactor;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<CategorieResponseDto>> createCategorie(@RequestBody @Valid CategorieRequestDto categorieRequestDto){

     CategorieResponseDto categorie = this.categorieService.createCategorie(categorieRequestDto);
        ApiResponse<CategorieResponseDto> response= this.apiResponseRefactor.response(
                categorie,
                "Catégorie ajoutée avec succes",
                null,
                true
        );
     return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // RECUPERATION DE TOUS LES CATEGORIES
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<CategorieResponseDto>>> getAllCategorie(){
         List<CategorieResponseDto> categories = this.categorieService.getAllCategorie();
        ApiResponse<List<CategorieResponseDto>> response= this.apiResponseRefactor.response(
                categories,
                "Catégorie récupérées avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }
}

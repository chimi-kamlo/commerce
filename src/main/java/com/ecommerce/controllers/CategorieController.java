package com.ecommerce.controllers;

import com.ecommerce.dto.CategorieRequestDto;
import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.services.CategorieService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "categories")
public class CategorieController {
    private  final CategorieService categorieService;

 // CREATION D'UNE CATEGORIE
    public CategorieController(CategorieService categorieService) {
        this.categorieService = categorieService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CategorieResponseDto> createCategorie(@RequestBody @Valid CategorieRequestDto categorieRequestDto){
     CategorieResponseDto categorie = this.categorieService.createCategorie(categorieRequestDto);
     return  ResponseEntity.status(HttpStatus.CREATED).body(categorie);
    }

    // RECUPERATION DE TOUS LES CATEGORIES
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<CategorieResponseDto> getAllCategorie(){
        return this.categorieService.getAllCategorie();

    }
}

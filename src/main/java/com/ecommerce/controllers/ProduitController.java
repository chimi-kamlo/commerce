package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.dto.ProduitRequestDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.entities.Produit;
import com.ecommerce.services.ProduitService;
import com.ecommerce.utils.ApiResponseRefactor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
@Slf4j
@RestController
@RequestMapping(path = "produits")
public class ProduitController {
    private final ProduitService produitService;
    private  final ApiResponseRefactor apiResponseRefactor;

    public ProduitController(ProduitService produitService, ApiResponseRefactor apiResponseRefactor) {
        this.produitService = produitService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    //CREATION D'UN PRODUIT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ProduitResponseDto>> createroduit(@RequestBody @Valid ProduitRequestDto produit){

        ApiResponse<ProduitResponseDto> response= this.apiResponseRefactor.response(
                this.produitService.createroduit(produit),
                "Produit ajouté avec succes",
                null,
                true
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // RECUPERATION DE TOUS LES PRODUITS
     @GetMapping
     @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<List<ProduitResponseDto>> >getAllProduit(){
        List<ProduitResponseDto> produits = this.produitService.getAllProduit();
         ApiResponse<List<ProduitResponseDto>> response= this.apiResponseRefactor.response(
                 produits,
                 "Produits récupérés avec succes",
                 null,
                 true
         );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    //RECUPERATION D'UN PRODUIT

    @ResponseStatus(HttpStatus.ACCEPTED)
   @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponse<ProduitResponseDto>> getProduit( @PathVariable int id){
       ProduitResponseDto  produit = this.produitService.getProduit(id);
        ApiResponse<ProduitResponseDto> response= this.apiResponseRefactor.response(
                produit,
                "Produit récupéré avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    // METTRE UN PRODUIT A JOUR
    @PutMapping( path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponse<ProduitResponseDto>> updateProduit(@PathVariable int id,@RequestBody @Valid ProduitRequestDto produitrequest){
 ProduitResponseDto produit =  this.produitService.updateProduit(id,produitrequest);
        ApiResponse<ProduitResponseDto> response= this.apiResponseRefactor.response(
                produit,
                "Produit mis à jour avec succes",
                null,
                true
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
response

        );
    }

    // SUPPRESSION D'UN PRODUIT
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delecteProduit(@PathVariable int id){
    this.produitService.delecteProduit(id);
    }

    //AJOUTER UNE IMAGE A UN PRODUIT
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(path= "/{produit_id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <ApiResponse<String>> addFile( @PathVariable int produit_id ,@RequestParam("fichier") MultipartFile fichier) throws IOException {

     this.produitService.addFile(produit_id,fichier);
        ApiResponse<String> response= this.apiResponseRefactor.response(
                null,
                "Image atouté  avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    // RECUPERER L'IMAGE D'UN PRODUIT
   @ResponseStatus(HttpStatus.ACCEPTED)
   @GetMapping("/{produit_id}/image")
    public ResponseEntity <Resource> getProduitImage(@PathVariable int produit_id) throws IOException {
      Resource resource =this.produitService.getProduitImage(produit_id);
     Path path= this.produitService.getProduitImagePath(produit_id);
       String type = Files.probeContentType(path);
       MediaType mediaType = type != null
               ? MediaType.parseMediaType(type)
               : MediaType.APPLICATION_OCTET_STREAM;

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .contentType(mediaType)
                .body(this.produitService.getProduitImage(produit_id));
    }
}

package com.ecommerce.controllers;

import com.ecommerce.dto.ProduitRequestDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.entities.Produit;
import com.ecommerce.services.ProduitService;
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

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }

    //CREATION D'UN PRODUIT
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProduitResponseDto> createroduit(@RequestBody @Valid ProduitRequestDto produit){
        ProduitResponseDto produitResponseDto = this.produitService.createroduit(produit);


        return ResponseEntity.status(HttpStatus.CREATED).body(produitResponseDto);
    }

    // RECUPERATION DE TOUS LES PRODUITS
     @GetMapping
     @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<List<ProduitResponseDto> >getAllProduit(){
        List<ProduitResponseDto> produits = this.produitService.getAllProduit();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produits);
    }

    //RECUPERATION D'UN PRODUIT

    @ResponseStatus(HttpStatus.ACCEPTED)
   @GetMapping(path = "/{id}")
    public ResponseEntity<ProduitResponseDto> getProduit( @PathVariable int id){
       ProduitResponseDto  produit = this.produitService.getProduit(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(produit);
    }

    // METTRE UN PRODUIT A JOUR
    @PutMapping( path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ProduitResponseDto> updateProduit(@PathVariable int id,@RequestBody @Valid ProduitRequestDto produitrequest){
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(

                this.produitService.updateProduit(id,produitrequest)
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
    public ResponseEntity <String> addFile( @PathVariable int produit_id ,@RequestParam("fichier") MultipartFile fichier) throws IOException {

     this.produitService.addFile(produit_id,fichier);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("succes");
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

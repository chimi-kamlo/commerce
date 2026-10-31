package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponseDto;
import com.ecommerce.dto.CategorieRequestDto;
import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.services.CategorieService;
import com.ecommerce.utils.ApiResponseRefactor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "categories")
@Tag(name = "CATEGORIES")
@SecurityRequirement(name = "bearerAuth")
public class CategorieController {
    private  final CategorieService categorieService;
    private final ApiResponseRefactor apiResponseRefactor;


    public CategorieController(CategorieService categorieService, ApiResponseRefactor apiResponseRefactor) {
        this.categorieService = categorieService;
        this.apiResponseRefactor = apiResponseRefactor;
    }
    // CREATION D'UNE CATEGORIE
    @Operation( description = "Permet d'enregistrer une catégorie de produit  ",
            summary = "Enregistrer une catégorie de produit "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Enregistrement effectuè avec succès"

            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "vous n'etes pas Authorisé à effectuer cette action",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Vous n'avez pas les autorisations nécessaires",
                               "data": null,
                               "errors": null
                             }
            """))),


            @ApiResponse(
                    responseCode = "401",
                    description = "vous n'etes pas authentifié",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Le token a expiré",
                               "data": null,
                               "errors": null
                             }
            """))),


    })
    @PreAuthorize("hasAuthority('CATEGORIE_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<CategorieResponseDto>> createCategorie(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires à l'enregistrement d'une catégorie",
                    required = true

            )
            @RequestBody @Valid CategorieRequestDto categorieRequestDto
    ){

     CategorieResponseDto categorie = this.categorieService.createCategorie(categorieRequestDto);
        ApiResponseDto<CategorieResponseDto> response= this.apiResponseRefactor.response(
                categorie,
                "Catégorie ajoutée avec succes",
                null,
                true
        );
     return  ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // RECUPERATION DE TOUS LES CATEGORIES
    @Operation( description = "Permet la récupération de toutes les Catégories de produit  ",
            summary = "récupéré la liste des Catégories de produit"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Catégories récupérés avec succès"

            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "vous n'etes pas Authorisé à effectuer cette action",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Vous n'avez pas les autorisations nécessaires",
                               "data": null,
                               "errors": null
                             }
            """))),


            @ApiResponse(
                    responseCode = "401",
                    description = "vous n'etes pas authentifié",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Le token a expiré",
                               "data": null,
                               "errors": null
                             }
            """))),


    })
    @PreAuthorize("hasAuthority('CATEGORIE_READ')")
    @GetMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponseDto<List<CategorieResponseDto>>> getAllCategorie(){
         List<CategorieResponseDto> categories = this.categorieService.getAllCategorie();
        ApiResponseDto<List<CategorieResponseDto>> response= this.apiResponseRefactor.response(
                categories,
                "Catégorie récupérées avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);

    }
}

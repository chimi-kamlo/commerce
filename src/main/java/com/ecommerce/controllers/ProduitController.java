package com.ecommerce.controllers;

import com.ecommerce.dto.ApiResponseDto;
import com.ecommerce.dto.ProduitRequestDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.services.ProduitService;
import com.ecommerce.utils.ApiResponseRefactor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
@Slf4j
@RestController
@Tag(name = "PRODUITS")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(path = "produits")
public class ProduitController {
    private final ProduitService produitService;
    private  final ApiResponseRefactor apiResponseRefactor;

    public ProduitController(ProduitService produitService, ApiResponseRefactor apiResponseRefactor) {
        this.produitService = produitService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    //ENREGISTREMENT D'UN PRODUIT
    @Operation(
            summary = "enregistrer un produit",
            description = "Permet d'enregistrer un nouveau produit ."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Produit enregistré avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Catégorie du  produit introuvable",
            content = @Content(
            mediaType = "application/json",
            examples = @ExampleObject(
                    name = "CategorieIntrouvable",
                    summary = "Code de catégorie inexistant",
                    value = """
                {
                    "success": false,
                    "message": "Catégorie introuvable",
                    "data": null,
                    "errors": null
                }
                """
            )
            )
            ),
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
            @ApiResponse(
                    responseCode = "400",
                    description = "Les données envoyées sont invalides",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "vos informations sont invalides",
                               "data": null,
                               "errors": null
                             }
            """))),

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
    })
    @PreAuthorize("hasAuthority('PRODUIT_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<ProduitResponseDto>> createroduit(
          @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description ="Données nécessaires à la création du produit",
                  required = true

          )
            @RequestBody @Valid ProduitRequestDto produit
    ){

        ApiResponseDto<ProduitResponseDto> response= this.apiResponseRefactor.response(
                this.produitService.createroduit(produit),
                "Produit ajouté avec succes",
                null,
                true
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // RECUPERATION DE TOUS LES PRODUITS
    @Operation( description = "Permet la récupération de tous  les produits  ",
            summary = "récupéré la liste des produits"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produits récupérés avec succès"

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
    @PreAuthorize("hasAuthority('PRODUIT_READ')")
     @GetMapping
     @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponseDto<List<ProduitResponseDto>>>getAllProduit(){
        List<ProduitResponseDto> produits = this.produitService.getAllProduit();
         ApiResponseDto<List<ProduitResponseDto>> response= this.apiResponseRefactor.response(
                 produits,
                 "Produits récupérés avec succes",
                 null,
                 true
         );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    //RECUPERATION D'UN PRODUIT
    @Operation(
            summary = "Récupérer un produit par son identifiant",
            description = "Permet de récupérer les informations d'un produit à partir de son identifiant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produit récupéré avec succès"

            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Produit introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Produit introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            ),
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

    })
    @PreAuthorize("hasAuthority('PRODUIT_READ')")
    @ResponseStatus(HttpStatus.ACCEPTED)
   @GetMapping(path = "/{id}")
    public ResponseEntity<ApiResponseDto<ProduitResponseDto>> getProduit(
            @Parameter(
                    description = "Identifiant unique du produit",
                    example = "15",
                    required = true
            )
            @PathVariable int id
    ){
       ProduitResponseDto  produit = this.produitService.getProduit(id);
        ApiResponseDto<ProduitResponseDto> response= this.apiResponseRefactor.response(
                produit,
                "Produit récupéré avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    // METTRE UN PRODUIT A JOUR
    @Operation( description = "Permet de  modifier les informations d'un produit  ",
            summary = "Modifier les informations d'un produit"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = " Informations produit Modifié avec succès"

            ),
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
                    responseCode = "404",
                    description = "Produit introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Produit introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            ),

    })
    @PreAuthorize("hasAuthority('PRODUIT_UPDATE')")
    @PutMapping( path = "/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ApiResponseDto<ProduitResponseDto>> updateProduit(
            @Parameter(
                    description = "Identifiant du produit",
                    example = "12",
                    required = true
            )
            @PathVariable int id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires à la modification du produit",
                    required = true

            )
            @RequestBody @Valid ProduitRequestDto produitrequest){
 ProduitResponseDto produit =  this.produitService.updateProduit(id,produitrequest);
        ApiResponseDto<ProduitResponseDto> response= this.apiResponseRefactor.response(
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
    @Operation( description = "Permet de  supprimer un produit  ",
            summary = "Supprimer un produit"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Produit supprimé avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "produit introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "produit introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            ),
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

    })
    @PreAuthorize("hasAuthority('PRODUIT_DELETE')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public void delecteProduit(
            @Parameter(
                    description = "Identifiant unique du produit",
                    example = "15",
                    required = true
            )
            @PathVariable int id
    ){
    this.produitService.delecteProduit(id);
    }

    //AJOUTER UNE IMAGE A UN PRODUIT
    @Operation( description = "Ajouter une image à  un produit existant  ",
            summary = "Ajouter une image à  un produit"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Image ajoutée avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "produit introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "produit introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            ),
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

    })
    @PreAuthorize("hasAuthority('PRODUIT_UPDATE')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PostMapping(path= "/{produit_id}/image",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity <ApiResponseDto<String>> addFile(@PathVariable int produit_id , @RequestParam("fichier") MultipartFile fichier) throws IOException {

     this.produitService.addFile(produit_id,fichier);
        ApiResponseDto<String> response= this.apiResponseRefactor.response(
                null,
                "Image atouté  avec succes",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    // VISUALISER L'IMAGE D'UN PRODUIT
    @Operation( description = "Permet de récuperé l'image d'un produit  ",
            summary = "récuper l'image d'un produit"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Image récupérée avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "produit introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                                                          {
                                                            "success": false,
                                                            "message": "Produit introuvable",
                                                            "data": null,
                                                            "errors": null
                                                          }
                                         """))
            ),
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
   @ResponseStatus(HttpStatus.ACCEPTED)
    @PreAuthorize("hasAuthority('PRODUIT_READ')")
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

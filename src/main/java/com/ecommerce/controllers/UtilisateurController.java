package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.security.AuthenticationService;
import com.ecommerce.security.JwtService;
import com.ecommerce.services.UtilisateurService;
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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@Tag(name = "UTILISATEUR")
@RequestMapping("utilisateurs")
public class UtilisateurController {
    private  final UtilisateurService utilisateurService;
    private final AuthenticationService authenticationSerice;
    private final JwtService jwtService;
    private final ApiResponseRefactor apiResponseRefactor;


    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationService authenticationSerice, JwtService jwtService, ApiResponseRefactor apiResponseRefactor) {
        this.utilisateurService = utilisateurService;
        this.authenticationSerice = authenticationSerice;

        this.jwtService = jwtService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    // ENREGISTREMENT D'UN UTILISATEUR EN BASE DE DONNEE
     @Operation(
             description = "Permet l'inscription  des utilisateurs  ",
             summary = "Inscription des utilisateurs"

     )
     @ApiResponses({
             @ApiResponse(
                     responseCode = "201",
                     description = "Utilisateur enrégistré avec succès"

             ),
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
            """)))

     })
    @PostMapping("inscription")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponseDto<UtilisateurResponseDto>> createUtilisateur(
             @io.swagger.v3.oas.annotations.parameters.RequestBody(
                     description ="Données nécessaires à l'enregistrement d'un utilisateur",
                     required = true

             )
            @RequestBody @Valid UtilisateurRequestDto utilisateurRequestDto
     ){
        UtilisateurResponseDto utilisateur = this.utilisateurService.createUtilisateur(utilisateurRequestDto);

        ApiResponseDto<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
                utilisateur,
                "Enregistrement  Reussi",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);

    }

    // RECUPERATION DE TOUS LES UTILISATEURS
    @Operation( description = "Permet la récupération de tous  les utilisateurs  ",
            summary = "récupéré la liste des utilisateurs"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateurs récupérés avec succès"

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
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<List<UtilisateurResponseDto>>> getAllUtilisateur(){
        List<UtilisateurResponseDto> utilisateurs= this.utilisateurService.getAllUtilisateur();


        ApiResponseDto<List<UtilisateurResponseDto>> response= this.apiResponseRefactor.response(
                utilisateurs,
                "Récupération réussie.",
                null,
                true
        );
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }



    //RECUPERATION D'UN UTILISATEUR
    @Operation( description = "Permet la récupération les informations d'un utilisateur  à partir de son identifiant  ",
            summary = "récupéré un utilisateur par son identifiant"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur récupéré avec succès"

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
                    description = "utilisateur introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            )

    })
    @SecurityRequirement(name = "bearerAuth")
   @GetMapping(path = "/{utilisateur_id}")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <ApiResponseDto<UtilisateurResponseDto>> getUtilisateur(
            @Parameter(
                    description = "Identifiant de l'utilisateur",
                    example = "12",
                    required = true
            )
            @PathVariable int utilisateur_id
    ){
       UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.getUtilisateur(utilisateur_id);
       ApiResponseDto<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
               utilisateurResponseDto,
               "Récupération réussie.",
               null,
               true
       );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // MISE A JOUR D'UN UTILISATEUR
    @Operation( description = "Permet de  modifier les informations d'un utilisateur  ",
            summary = "Modifier les informations d'un utilisateur"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = " informations utilisateur Modifié avec succès"

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
                    description = "utilisateur introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            ),

    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{utilisateur_id}")
    public ResponseEntity<ApiResponseDto<UtilisateurResponseDto>> updateUtilisateur(
            @Parameter(
                    description = "Identifiant de l'utilisateur",
                    example = "12",
                    required = true
            )
            @PathVariable int utilisateur_id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires à l'enregistrement d'un utilisateur",
                    required = true

            )
            @RequestBody @Valid UtilisateurRequestDto utilisateurDto){
 UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.updateUtilisateur(utilisateur_id,utilisateurDto);
        ApiResponseDto<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
                utilisateurResponseDto,
                "Mise a jour  réussie.",
                null,
                true
        );
 return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    //SUPPRIMER UN UTILISATEUR
    @Operation( description = "Permet de supprimer un utilisateur  ",
            summary = "supprimer un  utilisateur"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur supprimé avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "utilisateur introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
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
    @SecurityRequirement(name = "bearerAuth")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{utilisateur_id}")
    public ResponseEntity<ApiResponseDto<String>> deleteUtilisateur(
            @Parameter(
                    description = "Identifiant de l'utilisateur",
                    example = "12",
                    required = true
            )
            @PathVariable int utilisateur_id){
        this.utilisateurService.deleteUtilisateur(utilisateur_id);
        ApiResponseDto<String> response= this.apiResponseRefactor.response(
                null,
                "suppréssion reussie",
                null,
                true
        );
        return ResponseEntity.ok(response);
    }


    // AUTHENTIFICATION D'UN
    @Operation( description = "Permet d'authentifier un utilisateur  ",
            summary = "Authentification des utilisateurs"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur authentifié avec succès"

            ),
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
            """)))
    })
    @SecurityRequirement(name = "bearerAuth")
   @PostMapping("Authentication")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<Map<String,String>>>Authentification(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires à l'authentification  d'un utilisateur",
                    required = true

            )
            @RequestBody AuthenticationDto authenticationDto){

            Map<String,String> token= this.authenticationSerice.Authentification(authenticationDto);
       ApiResponseDto<Map<String,String>> response= this.apiResponseRefactor.response(
               token,
               "Authentification  réussie.",
               null,
               true
       );
            return ResponseEntity.ok().body(response);
    }

    // deconnexion utilisateur
    @Operation( description = "Permet la déconnexion d'un utilisateur  ",
            summary = "déconnecter  utilisateur"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Utilisateur déconnecté avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "utilisateur introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
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
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("deconnexion")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<String>> deconnexion(){
        this.jwtService.deconnexion();
        ApiResponseDto<String> response= this.apiResponseRefactor.response(
                null,
                "deconnecté avec Succes",
                null,
                true
        );
    return ResponseEntity.ok().body(response);
    }

    // AJOUTER UN PROFIL A L'UTILISATEUR
    @Operation( description = "Permet d'ajouter des information suplementaires sur utilisateur  ",
            summary = "Ajouter le profil  utilisateur"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Profil ajouté avec succès"

            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "utilisateur introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
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
            @ApiResponse(responseCode = "413", description = "La taille du fichier ne doit pas dépasser 2 Mo",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseDto.class),
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "le fichier ne doit pas dépasser 2 Mo  ",
                               "data": null,
                               "errors": null
                             }
            """)))


    })
    @SecurityRequirement(name = "bearerAuth")
     @PatchMapping(path = "profil",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<String>> addUtilisateurProfil(
            @Parameter(
                    description = "Image de l'utilisateur à téléverser",
                    required = true
            )
            @RequestPart("image") MultipartFile image,
            @Parameter(
                    description = "Description de l'utilisateur "

            )
            @RequestPart("description") ProfilRequestDto description) throws IOException {
        this.utilisateurService.addUtilisateurProfil(image,description);
         ApiResponseDto<String> response= this.apiResponseRefactor.response(
                 null,
                 "profil ajouté avec succes",
                 null,
                 true
         );
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
     }


     //RECUPER LE PROFIL DE L'UTILISATEUR CONNECTE
     @Operation( description = "Permet de récuperé le profil de l'utilisateur  ",
             summary = "récuper le profil utilisateur"
     )
     @ApiResponses({
             @ApiResponse(
                     responseCode = "200",
                     description = "Profil récupéré avec succès"

             ),
             @ApiResponse(
                     responseCode = "404",
                     description = "utilisateur introuvable",
                     content = @Content(
                             mediaType = "application/json",
                             examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "utilisateur introuvable",
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
     @SecurityRequirement(name = "bearerAuth")
     @GetMapping("profil")
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto< Map<String,String>>> getProfilUtilisateur() throws IOException {
         Map<String,String> profil =  this.utilisateurService.getProfilUtilisateur();
         ApiResponseDto<Map<String,String>> response= this.apiResponseRefactor.response(
                 profil,
                 "profil ajouté avec succes",
                 null,
                 true
         );
         return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                response
         );
     }


         // RECUPERER L'IMAGE D'UN UTILISATEUR
         @Operation( description = "Permet de récuperé l'image de profil d'un  utilisateur  ",
                 summary = "récuper l'image de profil d'un  utilisateur"
         )
         @ApiResponses({
                 @ApiResponse(
                         responseCode = "200",
                         description = "Image récupérée avec succès"

                 ),
                 @ApiResponse(
                         responseCode = "404",
                         description = "utilisateur introuvable",
                         content = @Content(
                                 mediaType = "application/json",
                                 examples = @ExampleObject(value = """
                                                          {
                                                            "success": false,
                                                            "message": "utilisateur introuvable",
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
         @SecurityRequirement(name = "bearerAuth")
         @ResponseStatus(HttpStatus.OK)
         @GetMapping("/profil/image")
         public ResponseEntity <Resource> getUtilisateurImage() throws IOException {

             Resource resource = this.utilisateurService.getUtilisateurImage();
             Path path= this.utilisateurService.getUtilisateurImagePath();
             String type = Files.probeContentType(path);
             MediaType mediaType = type != null
                     ? MediaType.parseMediaType(type)
                     : MediaType.APPLICATION_OCTET_STREAM;

             return ResponseEntity.status(HttpStatus.ACCEPTED)
                     .contentType(mediaType)
                     .body(resource);

}


}

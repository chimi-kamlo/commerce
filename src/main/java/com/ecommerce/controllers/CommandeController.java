package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.services.CommandeService;
import com.ecommerce.utils.ApiResponseRefactor;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("commandes")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "COMMANDES")
public class CommandeController {
    private final CommandeService commandeService;
    private final ApiResponseRefactor apiResponseRefactor;

    public CommandeController(CommandeService commandeService, ApiResponseRefactor apiResponseRefactor) {
        this.commandeService = commandeService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    // ENREGISTREMENT D'UNE COMMANDE
    @Hidden
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommandResponseDto> createCommande( ){
        CommandResponseDto commande = this.commandeService.createCommande();
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commande);

    }

    // PASSER UNE COMMANDE
    @Operation( description = "Permet de passer une commande de un ou plusieurs produits   ",
            summary = "passer une commande de un ou plusieurs produits "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Commande effectuée avec succès"

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
                    description = "produits introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "produits introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            )

    })
    @PostMapping("passerCommande")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<CommandResponseDto>> passerCommande(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires pour passer une commande",
                    required = true

            )
            @RequestBody PasserCommandeRequestDto passerCommandeRequestDto){
        CommandResponseDto commandResponseDto = this.commandeService.passerCommande(passerCommandeRequestDto.lignes());
        ApiResponseDto<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                "félicitation votre commande a été passée avec succes",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER UNE COMMANDE
    @Operation( description = "Permet de récupérer une commande en fonction de son identifiant   ",
            summary = " récupérer une commande en fonction de son identifiant"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Commande récupérée avec succès"

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
                    description = "Commande introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Commande introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            )

    })

    @GetMapping("/{commande_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<CommandResponseDto>> getCommande(
            @Parameter(
                    description = "Identifiant de la commande",
                    example = "12",
                    required = true
            )
            @PathVariable int commande_id){
        CommandResponseDto commandResponseDto = this.commandeService.getCommande(commande_id);
        ApiResponseDto<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                "félicitation votre commande a été récupérée avec succes",
                null,
                true
        );
        return  ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER TOUTES LES COMMANDES
    @Operation( description = "Permet la récupération de toutes  les commandes sous plusieurs pages ",
            summary = "récupéré la liste des commandes"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Commandes récupérés avec succès"

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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<ApiResponseDto<List<CommandResponseDto>>> getAllCommandes(
            @Parameter(
                    description = "Taille de la page",
                    example = "5",
                    required = false
            )
           @RequestParam(defaultValue = "10", required = false) int size,
            @Parameter(
                    description = "Numéro de la page",
                    example = "1",
                    required = false
            )
           @RequestParam(defaultValue = "1") int page,
           @RequestParam(defaultValue = "montant",required = false)  String montant,
            @Parameter(
                    description = "direction du tri",
                    example ="ASC",
                    required = false
            )
           @RequestParam(defaultValue = "ASC",required = false) String sortDir
    ){
        Sort sort=null;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(montant).ascending();
        }else {
            sort = Sort.by(montant).descending();
        }
        Pageable pageable = PageRequest.of(page,size,sort);



        List<CommandResponseDto> commandResponseDtos = this.commandeService.getAllCommandes(pageable);
        ApiResponseDto<List<CommandResponseDto>> response= this.apiResponseRefactor.response(
                commandResponseDtos,
                "Félicitations ! Vos commandes ont été récupérées avec succès",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR SPECIFIQUE
    @Operation( description = "Permet la récupération de toutes  les commandes sous plusieurs pages d'un utilisateur spécifiaue ",
            summary = "récupéré la liste des commandes d'un utilisateur spécifiaue"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Commandes récupérés avec succès"

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
    @GetMapping("commandeUtilisateur")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<List<CommandResponseDto>>> getAllCommandesSpesifiqueUtilisateur(
            @Parameter(
                    description = "Taille de la page",
                    example = "5",
                    required = false
            )
            @RequestParam(defaultValue = "10", required = false) int size,
            @Parameter(
                    description = "Numéro de la page",
                    example = "1",
                    required = false
            )
            @RequestParam(defaultValue = "1") int page,
            @Parameter(
                    description = "Motif de tri",
                    example ="montant",
                    required = false
            )
              @RequestParam(defaultValue = "montant",required = false)  String montant,
            @Parameter(
                    description = "direction du tri",
                    example ="ASC",
                    required = false
            )
            @RequestParam(defaultValue = "ASC",required = false) String sortDir
    ){
        Sort sort=null;
        if(sortDir.equalsIgnoreCase("ASC")){
            sort = Sort.by(montant).ascending();
        }else {
            sort = Sort.by(montant).descending();
        }
        Pageable pageable = PageRequest.of(page,size,sort);
        List<CommandResponseDto> commandResponseDtos = this.commandeService.getAllCommandesSpesifiqueUtilisateur(pageable);
        ApiResponseDto<List<CommandResponseDto>> response= this.apiResponseRefactor.response(
                commandResponseDtos,
                "Félicitations ! Vos commandes ont été récupérées avec succès",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // MODIFIER LE STATUT D'UNE COMMANDE
   @PatchMapping("statut")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponseDto<CommandResponseDto>> updateCommandeStatut(
            @RequestParam String commande_reference ,
            @RequestBody StatutRequestDto statut){
        CommandResponseDto commandResponseDto = this.commandeService.updateCommandeStatut(commande_reference,statut.statut());
       ApiResponseDto<CommandResponseDto> response= this.apiResponseRefactor.response(
               commandResponseDto,
               "Le statut de la commande a été modifié avec succes",
               null,
               true
       );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response) ;
    }


    // MODIFICATION D'UNE LIGNE COMMANDE
    @Operation( description = "Permet de modifier une ligneCommande en fonction de " +
            "la reference de la commande et de l'identifiant de la ligneCommande   ",
            summary = "  modifier une ligneCommande "
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = " LigneCommande modifiée avec succès"

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
                    description = "Commande introuvable",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = """
                             {
                               "success": false,
                               "message": "Commande introuvable",
                               "data": null,
                               "errors": null
                             }
            """))
            )

    })
    @PatchMapping("/{commande_reference}/ligne/{ligneCommande_id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<ApiResponseDto<CommandResponseDto>> updateLigneCommande(
            @Parameter(
                    description = "reference de la commande",
                    example ="550e8400-e29b-41d4-a716-446655440000",
                    required = true
            )
            @PathVariable String commande_reference,
            @Parameter(
                    description = "Identifiant de la ligneCommande",
                    example ="5",
                    required = true
            )
            @PathVariable int ligneCommande_id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description ="Données nécessaires à la modification de la ligne commande",
                    required = true

            )
            @RequestBody LignecommandeRequestDto lignecommandeRequestDto) {
      CommandResponseDto commandResponseDto = this.commandeService.updateLigneCommande(commande_reference,ligneCommande_id,lignecommandeRequestDto);
        ApiResponseDto<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                " Modification effectuée avec succes",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response) ;

    }

}

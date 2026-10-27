package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.entities.Commande;
import com.ecommerce.entities.StatutCommande;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.services.CommandeService;
import com.ecommerce.utils.ApiResponseRefactor;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("commandes")
public class CommandeController {
    private final CommandeService commandeService;
    private final ApiResponseRefactor apiResponseRefactor;

    public CommandeController(CommandeService commandeService, ApiResponseRefactor apiResponseRefactor) {
        this.commandeService = commandeService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    // ENREGISTREMENT D'UNE COMMANDE

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CommandResponseDto> createCommande( ){
        CommandResponseDto commande = this.commandeService.createCommande();
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(commande);

    }

    // PASSER UNE COMMANDE
    @PostMapping("passerCommande")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<CommandResponseDto>> passerCommande( @RequestBody PasserCommandeRequestDto passerCommandeRequestDto){
        CommandResponseDto commandResponseDto = this.commandeService.passerCommande(passerCommandeRequestDto.lignes());
        ApiResponse<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                "félicitation votre commande a été passée avec succes",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER UNE COMMANDE
    @GetMapping("/{commande_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<CommandResponseDto>> getCommande(@PathVariable int commande_id){
        CommandResponseDto commandResponseDto = this.commandeService.getCommande(commande_id);
        ApiResponse<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                "félicitation votre commande a été récupérée avec succes",
                null,
                true
        );
        return  ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<ApiResponse<List<CommandResponseDto>>> getAllCommandes(
           @RequestParam(defaultValue = "10", required = false) int size,
           @RequestParam(defaultValue = "1") int page,
           @RequestParam(defaultValue = "montant",required = false)  String montant,
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
        ApiResponse<List<CommandResponseDto>> response= this.apiResponseRefactor.response(
                commandResponseDtos,
                "Félicitations ! Vos commandes ont été récupérées avec succès",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR SPECIFIQUE
    @GetMapping("commandeUtilisateur")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<CommandResponseDto>>> getAllCommandesSpesifiqueUtilisateur(
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "1") int page,
              @RequestParam(defaultValue = "montant",required = false)  String montant,
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
        ApiResponse<List<CommandResponseDto>> response= this.apiResponseRefactor.response(
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
    public ResponseEntity<ApiResponse<CommandResponseDto>> updateCommandeStatut(@RequestParam String commande_reference ,@RequestBody StatutRequestDto statut){
        CommandResponseDto commandResponseDto = this.commandeService.updateCommandeStatut(commande_reference,statut.statut());
       ApiResponse<CommandResponseDto> response= this.apiResponseRefactor.response(
               commandResponseDto,
               "Le statut de la commande a été modifié avec succes",
               null,
               true
       );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response) ;
    }


    // MODIFICATION D'UNE LIGNE COMMANDE
    @PatchMapping("/{commande_reference}/ligne/{ligneCommande_id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<ApiResponse<CommandResponseDto>> updateLigneCommande(@PathVariable String commande_reference, @PathVariable int ligneCommande_id,@RequestBody LignecommandeRequestDto lignecommandeRequestDto) {
      CommandResponseDto commandResponseDto = this.commandeService.updateLigneCommande(commande_reference,ligneCommande_id,lignecommandeRequestDto);
        ApiResponse<CommandResponseDto> response= this.apiResponseRefactor.response(
                commandResponseDto,
                " Modification effectuée avec succes",
                null,
                true
        );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response) ;

    }

}

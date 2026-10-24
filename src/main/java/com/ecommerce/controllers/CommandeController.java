package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.entities.Commande;
import com.ecommerce.entities.StatutCommande;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.services.CommandeService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
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

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
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
    public ResponseEntity<CommandResponseDto> passerCommande( @RequestBody PasserCommandeRequestDto passerCommandeRequestDto){
        CommandResponseDto commandResponseDto = this.commandeService.passerCommande(passerCommandeRequestDto.lignes());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDto);
    }

    //PUBLIC RECUPERER UNE COMMANDE
    @GetMapping("/{commande_id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommandResponseDto> getCommande(@PathVariable int commande_id){
        CommandResponseDto commandResponseDto = this.commandeService.getCommande(commande_id);

        return  ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDto);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
   public ResponseEntity<List<CommandResponseDto>> getAllCommandes(){
        List<CommandResponseDto> commandResponseDtos = this.commandeService.getAllCommandes();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDtos);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR SPECIFIQUE
    @GetMapping("commandeUtilisateur")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<CommandResponseDto>> getAllCommandesSpesifiqueUtilisateur(){
        List<CommandResponseDto> commandResponseDtos = this.commandeService.getAllCommandesSpesifiqueUtilisateur();

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDtos);
    }

    // MODIFIER LE STATUT D'UNE COMMANDE
   @PatchMapping("statut")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CommandResponseDto> updateCommandeStatut(@RequestParam String commande_reference ,@RequestBody StatutRequestDto statut){
        CommandResponseDto commandResponseDto = this.commandeService.updateCommandeStatut(commande_reference,statut.statut());

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDto) ;
    }


    // MODIFICATION D'UNE LIGNE COMMANDE
    @PatchMapping("/{commande_reference}/ligne/{ligneCommande_id}")
    @ResponseStatus(HttpStatus.OK)
    public  ResponseEntity<CommandResponseDto> updateLigneCommande(@PathVariable String commande_reference, @PathVariable int ligneCommande_id,@RequestBody LignecommandeRequestDto lignecommandeRequestDto) {
      CommandResponseDto commandResponseDto = this.commandeService.updateLigneCommande(commande_reference,ligneCommande_id,lignecommandeRequestDto);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(commandResponseDto) ;

    }

}

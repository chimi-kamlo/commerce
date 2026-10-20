package com.ecommerce.controllers;

import com.ecommerce.dto.UtilisateurRequestDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.services.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("utilisateurs")
public class UtilisateurController {
    private  final UtilisateurService utilisateurService;

    public UtilisateurController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    // ENREGISTREMENT D'UN UTILISATEUR EN BASE DE DONNEE

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UtilisateurResponseDto> createUtilisateur(@RequestBody @Valid UtilisateurRequestDto utilisateurRequestDto){
        UtilisateurResponseDto utilisateur = this.utilisateurService.createUtilisateur(utilisateurRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(utilisateur);

    }

    // RECUPERATION DE TOUS LES UTILISATEURS
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<UtilisateurResponseDto>> getAllUtilisateur(){
        List<UtilisateurResponseDto> utilisateurs= this.utilisateurService.getAllUtilisateur();

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(utilisateurs);
    }

    //RECUPERATION D'UN UTILISATEUR
   @GetMapping(path = "/{utilisateur_id}")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <UtilisateurResponseDto> getUtilisateur(@PathVariable int utilisateur_id){
       UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.getUtilisateur(utilisateur_id);

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(utilisateurResponseDto);
    }

    // MISE A JOUR D'UN UTILISATEUR
    @PutMapping("/{utilisateur_id}")
    public ResponseEntity<UtilisateurResponseDto> updateUtilisateur(@PathVariable int utilisateur_id, @RequestBody @Valid UtilisateurRequestDto utilisateurDto){
 UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.updateUtilisateur(utilisateur_id,utilisateurDto);
   return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(utilisateurResponseDto);
    }

    //SUPPRIMER UN UTILISATEUR
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{utilisateur_id}")
    public ResponseEntity<String> deleteUtilisateur(@PathVariable int utilisateur_id){
        this.utilisateurService.deleteUtilisateur(utilisateur_id);
        return ResponseEntity.ok("suppréssion reussie");
    }
}

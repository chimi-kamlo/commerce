package com.ecommerce.controllers;

import com.ecommerce.dto.AuthenticationDto;
import com.ecommerce.dto.ProfilRequestDto;
import com.ecommerce.dto.UtilisateurRequestDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.security.AuthenticationService;
import com.ecommerce.security.JwtService;
import com.ecommerce.services.UtilisateurService;
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
@RequestMapping("utilisateurs")
public class UtilisateurController {
    private  final UtilisateurService utilisateurService;
    private final AuthenticationService authenticationSerice;
    private final JwtService jwtService;


    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationService authenticationSerice, JwtService jwtService) {
        this.utilisateurService = utilisateurService;
        this.authenticationSerice = authenticationSerice;

        this.jwtService = jwtService;
    }

    // ENREGISTREMENT D'UN UTILISATEUR EN BASE DE DONNEE

    @PostMapping("inscription")
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


    // AUTHENTIFICATION D'UN UTILISATEUR
   @PostMapping("Authentication")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Map<String,String>>Authentification(@RequestBody AuthenticationDto authenticationDto){

            Map<String,String> token= this.authenticationSerice.Authentification(authenticationDto);
        return ResponseEntity.ok().body(token);
    }

    // deconnexion utilisateur

    @PostMapping("deconnexion")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deconnexion(){
        this.jwtService.deconnexion();
    return ResponseEntity.ok("deconnecté avec Succes");
    }

    // AJOUTER UN PROFIL A L'UTILISATEUR
     @PatchMapping(path = "profil",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> addUtilisateurProfil(@RequestPart("image") MultipartFile image,
                                                       @RequestPart("description") ProfilRequestDto description) throws IOException {
        this.utilisateurService.addUtilisateurProfil(image,description);
         return ResponseEntity.status(HttpStatus.ACCEPTED).body("succes");
     }


     //RECUPER LE PROFIL DE L'UTILISATEUR CONNECTE
     @GetMapping("profil")
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity< Map<String,String>> getProfilUtilisateur() throws IOException {

         return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(
                 this.utilisateurService.getProfilUtilisateur()
         );
     }


         // RECUPERER L'IMAGE D'UN UTILISATEUR
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

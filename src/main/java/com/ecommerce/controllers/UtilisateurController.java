package com.ecommerce.controllers;

import com.ecommerce.dto.*;
import com.ecommerce.security.AuthenticationService;
import com.ecommerce.security.JwtService;
import com.ecommerce.services.UtilisateurService;
import com.ecommerce.utils.ApiResponseRefactor;
import jakarta.validation.Valid;
import org.jspecify.annotations.NonNull;
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
    private final ApiResponseRefactor apiResponseRefactor;


    public UtilisateurController(UtilisateurService utilisateurService, AuthenticationService authenticationSerice, JwtService jwtService, ApiResponseRefactor apiResponseRefactor) {
        this.utilisateurService = utilisateurService;
        this.authenticationSerice = authenticationSerice;

        this.jwtService = jwtService;
        this.apiResponseRefactor = apiResponseRefactor;
    }

    // ENREGISTREMENT D'UN UTILISATEUR EN BASE DE DONNEE

    @PostMapping("inscription")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<UtilisateurResponseDto>> createUtilisateur(@RequestBody @Valid UtilisateurRequestDto utilisateurRequestDto){
        UtilisateurResponseDto utilisateur = this.utilisateurService.createUtilisateur(utilisateurRequestDto);

        ApiResponse<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
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
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<List<UtilisateurResponseDto>>> getAllUtilisateur(){
        List<UtilisateurResponseDto> utilisateurs= this.utilisateurService.getAllUtilisateur();
//        ApiResponse<List<UtilisateurResponseDto>> response = ResponseRefactor(
//                utilisateurs,
//                "Récupération réussie.");

        ApiResponse<List<UtilisateurResponseDto>> response= this.apiResponseRefactor.response(
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
   @GetMapping(path = "/{utilisateur_id}")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity <ApiResponse<UtilisateurResponseDto>> getUtilisateur(@PathVariable int utilisateur_id){
       UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.getUtilisateur(utilisateur_id);
       ApiResponse<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
               utilisateurResponseDto,
               "Récupération réussie.",
               null,
               true
       );
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    // MISE A JOUR D'UN UTILISATEUR
    @PutMapping("/{utilisateur_id}")
    public ResponseEntity<ApiResponse<UtilisateurResponseDto>> updateUtilisateur(@PathVariable int utilisateur_id, @RequestBody @Valid UtilisateurRequestDto utilisateurDto){
 UtilisateurResponseDto utilisateurResponseDto = this.utilisateurService.updateUtilisateur(utilisateur_id,utilisateurDto);
        ApiResponse<UtilisateurResponseDto> response= this.apiResponseRefactor.response(
                utilisateurResponseDto,
                "Mise a jour  réussie.",
                null,
                true
        );
 return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    //SUPPRIMER UN UTILISATEUR
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{utilisateur_id}")
    public ResponseEntity<ApiResponse<String>> deleteUtilisateur(@PathVariable int utilisateur_id){
        this.utilisateurService.deleteUtilisateur(utilisateur_id);
        ApiResponse<String> response= this.apiResponseRefactor.response(
                null,
                "suppréssion reussie",
                null,
                true
        );
        return ResponseEntity.ok(response);
    }


    // AUTHENTIFICATION D'UN UTILISATEUR
   @PostMapping("Authentication")
   @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<Map<String,String>>>Authentification(@RequestBody AuthenticationDto authenticationDto){

            Map<String,String> token= this.authenticationSerice.Authentification(authenticationDto);
       ApiResponse<Map<String,String>> response= this.apiResponseRefactor.response(
               token,
               "Authentification  réussie.",
               null,
               true
       );
            return ResponseEntity.ok().body(response);
    }

    // deconnexion utilisateur

    @PostMapping("deconnexion")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<String>> deconnexion(){
        this.jwtService.deconnexion();
        ApiResponse<String> response= this.apiResponseRefactor.response(
                null,
                "deconnecté avec Succes",
                null,
                true
        );
    return ResponseEntity.ok().body(response);
    }

    // AJOUTER UN PROFIL A L'UTILISATEUR
     @PatchMapping(path = "profil",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<String>> addUtilisateurProfil(@RequestPart("image") MultipartFile image,
                                                       @RequestPart("description") ProfilRequestDto description) throws IOException {
        this.utilisateurService.addUtilisateurProfil(image,description);
         ApiResponse<String> response= this.apiResponseRefactor.response(
                 null,
                 "profil ajouté avec succes",
                 null,
                 true
         );
         return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
     }


     //RECUPER LE PROFIL DE L'UTILISATEUR CONNECTE
     @GetMapping("profil")
     @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse< Map<String,String>>> getProfilUtilisateur() throws IOException {
         Map<String,String> profil =  this.utilisateurService.getProfilUtilisateur();
         ApiResponse<Map<String,String>> response= this.apiResponseRefactor.response(
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

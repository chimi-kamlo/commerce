package com.ecommerce.controllers.advice;

import com.ecommerce.dto.ApiResponse;
import com.ecommerce.errors.RessourceIntrouvableException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;



import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ApplicationException {

    // VALIDATION DES DONNEES
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> gererErreursValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> erreurs = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erreur ->
                erreurs.putIfAbsent(
                        erreur.getField(),
                        erreur.getDefaultMessage()
                )
        );

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Les données envoyées sont invalides",
                null,
                erreurs
        );

        return ResponseEntity.badRequest().body(response);
    }

    // GERE LES ARGUMENTS INATTENDUS

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> gererArgumentInvalide(
            IllegalArgumentException ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                null
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // GERE LES ELEMENT INTROUVABLE

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> gererEntiteIntrouvable(
            EntityNotFoundException ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // ressource introuvable

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ResponseEntity<ApiResponse<Void>> gererRessourceIntrouvable(RessourceIntrouvableException ex){
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    // TAILLE DU FICHIER SUPPERIEUR A LA DEMANDE
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> gererMaxUploadSize(MaxUploadSizeExceededException ex){

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.CONTENT_TOO_LARGE)
                .body(response);
    }

     //JSON MAL FORME
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> gererHttpMessageNotReadable(HttpMessageNotReadableException ex){
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                ex.getMessage(),
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);

    }

    // ACCES REFUSE
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiResponse<Void>>gererAccessDenied(AccessDeniedException ex){
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Vous n'avez pas les autorisations nécessaires",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);

    }

    // INFORMATION INCORECTS
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiResponse<Void>>gererAccessDenied(BadCredentialsException ex){
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Identifiants incorrects",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);

    }

    // COMPTE NON ACTIVE

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ApiResponse<Void>>gererAccessDenied(DisabledException ex){
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Compte désactivé",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);

    }

    //JWT EXPIRE
    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<ApiResponse<Void>>gererExpiredJwt(ExpiredJwtException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Le token a expiré",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }

    //SIGNATURE DU JWT
    @ExceptionHandler(SignatureException.class)
    ResponseEntity<ApiResponse<Void>>gererSignature(SignatureException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "La signature de votre token est incorect",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }
// JWT MAL FORME
    @ExceptionHandler(MalformedJwtException.class)
    ResponseEntity<ApiResponse<Void>>gererMalformedJwt(MalformedJwtException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Token mal formé",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }

// ERREUR D'AUTHENTIFICATION

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<Void>> gererAuthentification(
            AuthenticationException ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Authentification requise ou invalide",
                null,
                null
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }













// EXCEPTION GLOBALE

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> gererExceptionGenerale(
            Exception ex) {

        ApiResponse<Void> response = new ApiResponse<>(
                false,
                "Une erreur interne est survenue",
                null,
                null
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

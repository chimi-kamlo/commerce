package com.ecommerce.controllers.advice;

import com.ecommerce.dto.ApiResponseDto;
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
import org.springframework.security.web.access.AccessDeniedHandler;
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
    public ResponseEntity<ApiResponseDto<Void>> gererErreursValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> erreurs = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(erreur ->
                erreurs.putIfAbsent(
                        erreur.getField(),
                        erreur.getDefaultMessage()
                )
        );

        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                "Les données envoyées sont invalides",
                null,
                erreurs
        );

        return ResponseEntity.badRequest().body(response);
    }

    // GERE LES ARGUMENTS INATTENDUS


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponseDto<Void>> gererArgumentInvalide(
            IllegalArgumentException ex) {

        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    public ResponseEntity<ApiResponseDto<Void>> gererEntiteIntrouvable(
            EntityNotFoundException ex) {

        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    public ResponseEntity<ApiResponseDto<Void>> gererRessourceIntrouvable(RessourceIntrouvableException ex){
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    public ResponseEntity<ApiResponseDto<Void>> gererMaxUploadSize(MaxUploadSizeExceededException ex){

        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    public ResponseEntity<ApiResponseDto<Void>> gererHttpMessageNotReadable(HttpMessageNotReadableException ex){
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    ResponseEntity<ApiResponseDto<Void>>gererAccessDenied(AccessDeniedException ex){
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    ResponseEntity<ApiResponseDto<Void>>gererAccessDenied(BadCredentialsException ex){
        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                "Identifiants incorrects",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }

    // COMPTE NON-ACTIVE

    @ExceptionHandler(DisabledException.class)
    ResponseEntity<ApiResponseDto<Void>>gererAccessDenied(DisabledException ex){
        ApiResponseDto<Void> response = new ApiResponseDto<>(
                false,
                "Compte désactivé",
                null,
                null
        );
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);

    }

    //JWT EXPIRE
    @ExceptionHandler(ExpiredJwtException.class)
    ResponseEntity<ApiResponseDto<Void>>gererExpiredJwt(ExpiredJwtException ex) {
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    ResponseEntity<ApiResponseDto<Void>>gererSignature(SignatureException ex) {
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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
    ResponseEntity<ApiResponseDto<Void>>gererMalformedJwt(MalformedJwtException ex) {
        ApiResponseDto<Void> response = new ApiResponseDto<>(
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

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ApiResponseDto<Void>> gererAuthentification(
//            AuthenticationException ex) {
//
//        ApiResponseDto<Void> response = new ApiResponseDto<>(
//                false,
//                "Authentification requise ou invalide",
//                null,
//                null
//        );

//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(response);
//    }













// EXCEPTION GLOBALE

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto<Void>> gererExceptionGenerale(
            Exception ex) {

        ApiResponseDto<Void> response = new ApiResponseDto<>(
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

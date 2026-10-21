package com.ecommerce.security;

import com.ecommerce.entities.Utilisateur;
import com.ecommerce.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.Serializable;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private  String ENTCRIPTIO_KEY ;
 private  final UtilisateurService utilisateurService;

    public JwtService(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    public Map<String, String> generateToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
        return getJwt(utilisateur);
    }

    private Map<String, String> getJwt(Utilisateur utilisateur) {
        long currentTime = System.currentTimeMillis();
        Long expirationTime = currentTime+30*60*1000;

        // MISE EN PLACE DES DONNEES UTILISATEUR POUR LA GENERATION DU JWT
        Map<String, ? extends Serializable> claims = Map.of(
                "nom",utilisateur.getNom(),
                "role", (Serializable) utilisateur.getRole().getLibele(),
                "email",utilisateur.getEmail(),
                Claims.EXPIRATION,new Date(expirationTime),
                Claims.SUBJECT,utilisateur.getEmail()
                );
 // CONSTRUCTION DU JWTTOKEN
        final String bearer  = Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .claims(claims)
                .subject(utilisateur.getEmail())
                .signWith(getKey())
                .compact();
         return Map.of("Bearer",bearer);
    }

   // GENERATION DE LA CLE DE CRYPTAGE
    private Key getKey() {
        byte[] decode = Decoders.BASE64.decode(ENTCRIPTIO_KEY);
        return Keys.hmacShaKeyFor(decode);
    }
}

package com.ecommerce.security;

import com.ecommerce.entities.Jwt;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.repositories.JwtRepository;
import com.ecommerce.repositories.UtilisateurRepository;
import com.ecommerce.services.UtilisateurService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@Transactional
public class JwtService {
    @Value("${jwt.secret}")
    private  String ENTCRIPTIO_KEY ;
 private  final UtilisateurService utilisateurService;
 private final JwtRepository jwtRepository;
 private final UtilisateurRepository utilisateurRepository;

    public JwtService(UtilisateurService utilisateurService, JwtRepository jwtRepository, UtilisateurRepository utilisateurRepository) {
        this.utilisateurService = utilisateurService;
        this.jwtRepository = jwtRepository;

        this.utilisateurRepository = utilisateurRepository;
    }

    public Map<String, String> generateToken(String username) {
        Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);

        Map<String, String> jwtMap = getJwt(utilisateur);
      this.desactiveToken(utilisateur);
        // STOCKAGE DU JWT EN BASE DE DONNEES
        Jwt jwt = Jwt.builder()
                .desactive(false)
                .expire(false)
                .valeur(jwtMap.get("Bearer"))
                .utilisateur(utilisateur)
                .build();

    this.jwtRepository.save(jwt);

        return jwtMap;
    }



    public boolean IsExpired(String jwt) {
        Claims claims = getAllClaims(jwt);
        Date expiration = claims.getExpiration();
        return expiration.before(new Date());
    }


    public String extractUsername(String jwt) {
        Claims claims = getAllClaims(jwt);

        return claims.getSubject();
    }

    // RECUPERATION DU TOKEN EN BASE DE DONNEES

    public Jwt getBdToken(String token) {
     return  this.jwtRepository.findByValeurAndDesactiveAndExpire(token, false, false).orElseThrow(()-> new RuntimeException("Token Invalide"));
    }

    // EXTRACTION DES DONNEES DANS LE TOKEN
    private Claims getAllClaims(String jwt) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return  claims;
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

  // DESACTIVATION DES JwtToken
    private void desactiveToken(Utilisateur utilisateur) {
        List<Jwt> allJwt = this.jwtRepository.getAllUtilisateurToken(utilisateur.getEmail()).peek(
                (user) -> {

                    user.setDesactive(true);
                    user.setExpire(true);
                }
        ).collect(Collectors.toList());
        this.jwtRepository.saveAll(allJwt);

    }


    public void deconnexion() {
        Utilisateur utilisateur =(Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Jwt jwt = this.jwtRepository.getAllUtilisateuValideToken(utilisateur.getEmail(),false,false).orElseThrow(
                ()->new RuntimeException("Token Invalide"));
   jwt.setExpire(true);
        jwt.setDesactive(true);

        this.jwtRepository.save(jwt);
    }
}

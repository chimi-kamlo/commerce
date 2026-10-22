package com.ecommerce.repositories;

import com.ecommerce.entities.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface JwtRepository extends JpaRepository<Jwt,Integer> {
 // RECUPER UN JWT EN FONCTION DE LA VALEUR , EXPIRE ET DE DESACTIVE
 Optional<Jwt> findByValeurAndDesactiveAndExpire(String valeur,boolean desactive , boolean expire);

  //Optional<Jwt> findByValeurAndExpireAndDesactive(String valeur, boolean expire, boolean desactive);

  // RECUPER TOUS LES JWT D'UN UTILISATEUR EN FONCTION DE SON EMAIL
  @Query("FROM Jwt j WHERE  j.utilisateur.email=:email")
  Stream<Jwt> getAllUtilisateurToken(String email);

// RECUPER LE JWT NON EXPIRE ET NON DESACTIVE D'UN UTILISATEUR EN FONCTION DE L'EMAIL , EXPIRE ET DE DESACTIVE
  @Query("FROM Jwt j WHERE  j.utilisateur.email=:email AND j.expire=:expire AND j.desactive=:desactive")
  Optional<Jwt> getAllUtilisateuValideToken(String email,boolean expire,boolean desactive);
}

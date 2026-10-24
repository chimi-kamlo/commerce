package com.ecommerce.repositories;

import com.ecommerce.entities.Commande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface CommandeRepository extends JpaRepository<Commande,Integer> {

   // RECUPERER UNE COMMANDE EN FONCTION DE SON ID
    Optional<Commande> findCommandeById(int commande_id);



    // RECUPERER LA COMMANDE D'UN UTILISATEUR SPECIFIQUE
    List<Commande> findAllByUtilisateurId(int utilisateur_id);

    // RECUPERER UNE COMMANDE EN FONCTION DE SA REFERENCE
    Optional<Commande> findCommandeByReference( String commande_reference);

}


package com.ecommerce.repositories;

import com.ecommerce.entities.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande,Integer> {
 // RECUPERATION DE LA LIGNECOMMAN DE EN FONCTION DE L'ID
   Optional<LigneCommande> findLigneCommandeByCommandeIdAndId(int ligneCommande_id,int commande_id);
}

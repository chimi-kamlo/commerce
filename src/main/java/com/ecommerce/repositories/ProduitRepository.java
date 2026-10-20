package com.ecommerce.repositories;

import com.ecommerce.entities.Produit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ProduitRepository extends JpaRepository<Produit,Integer> {


    Optional<Produit> getProduitById(int produitId);
}

package com.ecommerce.repositories;

import com.ecommerce.entities.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategorieRepository extends JpaRepository<Categorie,Integer> {


    Optional<Categorie> findCategorieByCodeCategorie(String code);

}



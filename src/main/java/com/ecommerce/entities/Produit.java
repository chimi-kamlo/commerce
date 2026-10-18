package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,name = "nom_produit")
    private String nomProduit;
    @Column(nullable = false,precision = 8,scale = 2)
    private BigDecimal prix;
    @Column(nullable = false)
    private  long stock;
    @Column(nullable = false,name = "image_produit")
    private String imageProduit;
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "categorie_id",referencedColumnName = "id")
    private Categorie categorie;
    @OneToMany(
            mappedBy = "produit",
            cascade = CascadeType.PERSIST
    )
    private List<LigneCommande> ligneCommande = new ArrayList<>();

}

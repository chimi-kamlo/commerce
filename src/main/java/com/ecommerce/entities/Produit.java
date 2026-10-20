package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produits")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
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

    public Produit(String nomProduit, BigDecimal prix, long stock, Categorie categorie) {
        this.nomProduit = nomProduit;
        this.prix = prix;
        this.stock = stock;
        this.categorie = categorie;
    }


}

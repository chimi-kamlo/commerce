package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LigneCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int quantite;
    @Column(nullable = false)
    private BigDecimal prixUnitaire = BigDecimal.ZERO;
    @Column(nullable = false)
    private BigDecimal montant=BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "produit_id",referencedColumnName = "id",nullable = false)
    private Produit produit;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "commande_id",referencedColumnName = "id",nullable = false)
    private Commande commande;

    public LigneCommande(int quantite, BigDecimal prixUnitaire, Produit produit, Commande commande) {
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.produit = produit;
        this.commande = commande;
    }
}

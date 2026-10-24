package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "commandes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String reference;
    private LocalDateTime dateCommande;
    private LocalDateTime dateModification;
    private BigDecimal montantCommande=BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    private StatutCommande statut;
    @OneToMany(
            mappedBy = "commande",
            cascade = {CascadeType.PERSIST,CascadeType.MERGE}
    )
    private List<LigneCommande> ligneCommande = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id",nullable = false,referencedColumnName = "id")
    private Utilisateur utilisateur;

    public Commande(String reference, LocalDateTime dateCommande,
                    StatutCommande statut,
                    Utilisateur utilisateur) {
        this.reference = reference;
        this.dateCommande = dateCommande;
        this.statut = statut;
        this.utilisateur = utilisateur;
    }
}

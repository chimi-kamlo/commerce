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
    private BigDecimal montantCommande;
    @Enumerated(EnumType.STRING)
    private StatutCommande Statut;
    @OneToMany(
            mappedBy = "commande",
            cascade = CascadeType.PERSIST
    )
    private List<LigneCommande> ligneCommande = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id",nullable = false,referencedColumnName = "id")
    private Utilisateur utilisateur;
}

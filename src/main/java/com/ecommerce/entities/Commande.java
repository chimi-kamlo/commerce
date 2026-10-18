package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
    @JoinColumn(name = "client_id",referencedColumnName = "id",nullable = false)
    private Client client;


}

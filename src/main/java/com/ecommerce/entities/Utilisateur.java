package com.ecommerce.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String nom;
    @Column(unique = true,nullable = false)
    private String prenom;
    @Column(unique = true,nullable = false)
    private  String email;
    @Column(unique = true,nullable = false,name = "mot_de_passe")
    private String motDePasse;
    private boolean actif = true;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "utilisateur")
    private List<Commande> commandes = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "profil_id",unique = true,referencedColumnName = "id")
    private Profil profil;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Role role;

}

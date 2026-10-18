package com.ecommerce.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String nom;
    @Column(unique = true,nullable = false)
    private String prenom;
    @Column(unique = true,nullable = false)
    private  String email;
    @Column(unique = true,nullable = false)
    private String motDePasse;
    private boolean actif = true;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "client")
    private List<Commande> commande = new ArrayList<>();
    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "profil_id",unique = true,referencedColumnName = "id")
    private Profil profil;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Role role;
}

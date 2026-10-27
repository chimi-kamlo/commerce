package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "profils")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private String avatar;
    @OneToOne(mappedBy = "profil")
private Utilisateur utilisateur;

    public Profil(String description, String avatar, Utilisateur utilisateur) {
        this.description = description;
        this.avatar = avatar;
        this.utilisateur = utilisateur;
    }
}

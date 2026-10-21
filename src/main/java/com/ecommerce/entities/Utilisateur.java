package com.ecommerce.entities;


import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Utilisateur implements UserDetails {
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

    public Utilisateur(String nom, String prenom, String email, String motDePasse) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "utilisateur")
    private List<Commande> commandes = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true,cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    @JoinColumn(name = "profil_id",unique = true,referencedColumnName = "id")
    private Profil profil;

    @OneToOne(cascade = CascadeType.PERSIST)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + TypeRole.UTILISATEUR));
    }

    @Override
    public @Nullable String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return actif;
    }

    @Override
    public boolean isAccountNonLocked() {
        return actif;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return actif;
    }

    @Override
    public boolean isEnabled() {
        return actif;
    }
}

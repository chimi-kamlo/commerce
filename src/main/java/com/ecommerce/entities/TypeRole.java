package com.ecommerce.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public enum TypeRole {
    UTILISATEUR(Set.of(
            TypePermission.PASSER_COMMANDE,
            TypePermission.CATEGORIE_READ,
            TypePermission.COMMANDE_UTILISATEUR_READ,
            TypePermission.PRODUIT_READ

            )),
    ADMINISTRATEUR(Set.of(
            TypePermission.PASSER_COMMANDE,
            TypePermission.UTILISATEUR_READ,
            TypePermission.UTILISATEUR_DELETE,
            TypePermission.PRODUIT_CREATE,
            TypePermission.PRODUIT_READ,
            TypePermission.PRODUIT_UPDATE,
            TypePermission.PRODUIT_DELETE,
            TypePermission. CATEGORIE_CREATE,
            TypePermission. CATEGORIE_READ,
            TypePermission.CATEGORIE_UPDATE,
            TypePermission.CATEGORIE_DELETE,
            TypePermission.COMMANDE_READ,
            TypePermission.COMMANDE_UTILISATEUR_READ,
            TypePermission.COMMANDE_UPDATE_STATUT,
            TypePermission.COMMANDE_UPDATE
    ));

    @Getter
    private Set<TypePermission> permissions;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> simpleGrantedAuthorities = this.permissions.stream().map(
        permissions -> new SimpleGrantedAuthority(permissions.name())
        ).collect(Collectors.toList());
        simpleGrantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return simpleGrantedAuthorities;
    }
}


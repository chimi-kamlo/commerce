package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique = true,name = "code_categorie")
    private String codeCategorie;
    @Column(nullable = false,unique = true)
    private String libele;
    @OneToMany(fetch = FetchType.LAZY,mappedBy = "categorie")
    private List <Produit> produit=new ArrayList<>();

    public Categorie( String libele,String codeCategorie) {
        this.codeCategorie = codeCategorie;
        this.libele = libele;
    }
}

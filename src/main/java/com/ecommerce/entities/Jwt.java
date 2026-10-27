package com.ecommerce.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "jwt")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Jwt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private  Boolean expire;
    private Boolean desactive;
    @Column(length = 2048)
    private String valeur;
    @ManyToOne( cascade ={ CascadeType.DETACH,CascadeType.MERGE} )
    @JoinColumn(name = "utilisateur_id",referencedColumnName = "id")
    private Utilisateur utilisateur;

}

package com.ecommerce.mapper;

import com.ecommerce.dto.CommandResponseDto;
import com.ecommerce.dto.LigneCommandeResponseDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.Commande;
import com.ecommerce.entities.LigneCommande;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
@Component
public class CommandeDtoMapper implements Function<Commande, CommandResponseDto>  {
    @Override
    public CommandResponseDto apply(Commande commande){
    List<LigneCommandeResponseDto> lignes = new ArrayList<>();
if (commande.getLigneCommande()!=null ||!commande.getLigneCommande().isEmpty()){
    for(LigneCommande ligne:commande.getLigneCommande()){
        // INFORMATION SUR LE PRODUIT
        LigneCommandeResponseDto ligneCommande = getLigneCommande(ligne);
        lignes.add(ligneCommande);
    }


}
        return new CommandResponseDto(
                commande.getId(),
                commande.getReference(),
                commande.getDateCommande(),
                commande.getDateModification(),
                commande.getStatut(),
                commande.getMontantCommande(),
                lignes

        );
    }

    private static @NonNull LigneCommandeResponseDto getLigneCommande(LigneCommande ligne) {
        Map<String,Object> produit = new HashMap<>();
        produit.put("id", ligne.getProduit().getId());
        produit.put("nomProduit", ligne.getProduit().getNomProduit());
        produit.put("prix", ligne.getProduit().getPrix());
        produit.put("image_url", ligne.getProduit().getImageProduit());


        // INSTANTIATION D'UNE LIGNECOMMANDE
        return new LigneCommandeResponseDto(
              ligne.getId(),
              ligne.getQuantite(),
              ligne.getPrixUnitaire(),
              ligne.getMontant(),
                produit
        );
    }
}

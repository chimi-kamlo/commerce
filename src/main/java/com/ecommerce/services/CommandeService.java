package com.ecommerce.services;

import com.ecommerce.dto.CategorieResponseDto;
import com.ecommerce.dto.CommandResponseDto;
import com.ecommerce.dto.CommandeRequestDto;
import com.ecommerce.dto.LignecommandeRequestDto;
import com.ecommerce.entities.*;
import com.ecommerce.mapper.CommandeDtoMapper;
import com.ecommerce.mapper.GetAllCommandeDtoMapper;
import com.ecommerce.repositories.CommandeRepository;
import com.ecommerce.repositories.LigneCommandeRepository;
import com.ecommerce.repositories.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
@Slf4j
@Service
public class CommandeService {
    private final CommandeRepository commandeRepository;
    private final CommandeDtoMapper commandeDtoMapper;
    private  final ProduitRepository produitRepository;
    private final GetAllCommandeDtoMapper getAllCommandeDtoMapper;
    private final LigneCommandeRepository ligneCommandeRepository;

    public CommandeService(CommandeRepository commandeRepository, CommandeDtoMapper commandeDtoMapper, ProduitRepository produitRepository, GetAllCommandeDtoMapper getAllCommandeDtoMapper, LigneCommandeRepository ligneCommandeRepository) {

        this.commandeRepository = commandeRepository;
        this.commandeDtoMapper = commandeDtoMapper;
        this.produitRepository = produitRepository;
        this.getAllCommandeDtoMapper = getAllCommandeDtoMapper;
        this.ligneCommandeRepository = ligneCommandeRepository;
    }
    // ENREGISTREMENT D'UNE COMMANDE
    public CommandResponseDto createCommande(){
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Commande commande = Commande.builder()
                .reference(UUID.randomUUID().toString())
                .dateCommande(LocalDateTime.now())
                .statut(StatutCommande.EN_ATTENTE)
                .utilisateur(utilisateur)
                .build();
      Commande commandeSauvegardee =   this.commandeRepository.save(commande);
      return commandeDtoMapper.apply(commandeSauvegardee);

    }

    // PASSER UNE COMMANDE
    @Transactional
    public CommandResponseDto passerCommande(Set<LignecommandeRequestDto> lignecommandeRequestDtoSet){
        // JE RECUPER MON UTILISATEUR DANS LE CONTEXTE DE SECURITE
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Commande commande = new Commande(UUID.randomUUID().toString(), LocalDateTime.now(),
                StatutCommande.EN_ATTENTE, utilisateur);




           // J'INITIALISE LE MONTANT TOTAL DE LA COMMANDE
           BigDecimal montantTotal = BigDecimal.ZERO;

           for (LignecommandeRequestDto ligne : lignecommandeRequestDtoSet) {
               Produit produit = this.produitRepository.getProduitById(ligne.produit_id()).orElseThrow(
                       () -> new IllegalArgumentException("produit intouvable"));

               LigneCommande ligneCommande = new LigneCommande(ligne.quantite(), produit.getPrix(), produit, commande);

               // CALCUL DU PRIX total du produit EN FONCTION DE LA QUANTITE
               BigDecimal totalLigne = produit.getPrix().multiply(BigDecimal.valueOf(ligne.quantite()));

               // JE MOGIFIE LE MONTANT DE LA LIGNE COMMANDE
               ligneCommande.setMontant(totalLigne);

               montantTotal = montantTotal.add(totalLigne);

               // J'AJOUTE ICI LA LIGNECOMMANDE A MA COMMANDE
               commande.getLigneCommande().add(ligneCommande);

           }
               // AJOUT DU MONTANT TOTAL DE LA COMMANDE
        commande.setMontantCommande(montantTotal);
        Commande commandeSauvegardee = this.commandeRepository.save(commande);
        return  this.commandeDtoMapper.apply(commandeSauvegardee);
    }



    //PUBLIC RECUPERER UNE COMMANDE
    public CommandResponseDto getCommande(int commande_id){
  Commande commande = this.commandeRepository.findCommandeById(commande_id).orElseThrow(()->
      new EntityNotFoundException("commande introuvable")
  );
    return  commandeDtoMapper.apply(commande);
    }

    // RECUPERER TOUTES LES COMMANDES
    public List<CommandResponseDto> getAllCommandes(Pageable pageable){
      Page <Commande> commande =  this.commandeRepository.findAll(pageable);

        return this.getAllCommandeDtoMapper.apply(commande);
    }

    // RECUPERER TOUTES LES COMMANDES D'UN UTILISATEUR SPECIFIQUE

    public List<CommandResponseDto> getAllCommandesSpesifiqueUtilisateur(Pageable pageable){
        // JE RECUPER MON UTILISATEUR DANS LE CONTEXTE DE SECURITE
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Page <Commande> commande = this.commandeRepository.findAllByUtilisateurId(utilisateur.getId(),pageable);

        return this.getAllCommandeDtoMapper.apply(commande);
    }

    // MODIFIER LE STATUT D'UNE COMMANDE

    public CommandResponseDto updateCommandeStatut(String commande_reference ,StatutCommande statut){
      // RECUPERATION DE LA COMMANDE EN BASE DE DONNEE
        Commande commandeBd =  this.commandeRepository.findCommandeByReference(commande_reference).orElseThrow(()->
              new EntityNotFoundException("commande introuvable")
      );
        //  MODIFICATION DU STATUT de la commande
        commandeBd.setStatut(statut);

        // eregistrement de la commande

        Commande commandeSauvegargee = this.commandeRepository.save(commandeBd);

        return  this.commandeDtoMapper.apply(commandeSauvegargee);
    }

    // MODIFICATION D'UNE LIGNE COMMANDE
 @Transactional
  public CommandResponseDto updateLigneCommande(String commande_reference,int ligneCommande_id,LignecommandeRequestDto lignecommandeRequestDto){
      // RECUPERATION DE LA COMMANDE EN BASE DE DONNEE
      Commande commandeBd =  this.commandeRepository.findCommandeByReference(commande_reference).orElseThrow(()->
              new EntityNotFoundException("commande introuvable")
      );
      // RECUPERATION DE LA LIGNE COMMANDE EN BASE DE DONNEE
     LigneCommande ligneCommande = this.ligneCommandeRepository.findLigneCommandeByCommandeIdAndId(
             commandeBd.getId(),ligneCommande_id).orElseThrow(()->
             new EntityNotFoundException("la lignecommande est  introuvable")
     );
     // JE MODIFIE LA QUANTITE DE LA LIGNE COMMANDE PUIS J'ENREGISTRE LES MODIFICATIONS
    ligneCommande.setQuantite(lignecommandeRequestDto.quantite());
     this.ligneCommandeRepository.save(ligneCommande);

     // J'INITIALISE LE MONTANT TOTAL DE LA COMMANDE
     BigDecimal montantTotal = BigDecimal.ZERO;

     for (LigneCommande ligne : commandeBd.getLigneCommande()) {
         Produit produit = this.produitRepository.getProduitById(ligne.getProduit().getId()).orElseThrow(
                 () -> new IllegalArgumentException("produit intouvable"));


         // CALCUL DU PRIX total du produit EN FONCTION DE LA QUANTITE
         BigDecimal totalLigne = produit.getPrix().multiply(BigDecimal.valueOf(ligne.getQuantite()));

         // JE MOGIFIE LE MONTANT DE LA LIGNE COMMANDE
         ligneCommande.setMontant(totalLigne);

         montantTotal = montantTotal.add(totalLigne);


     }
     // AJOUT DU MONTANT TOTAL DE LA COMMANDE
     commandeBd.setMontantCommande(montantTotal);
     Commande commandeSauvegardee = this.commandeRepository.save(commandeBd);
     return  this.commandeDtoMapper.apply(commandeSauvegardee);

  }

}

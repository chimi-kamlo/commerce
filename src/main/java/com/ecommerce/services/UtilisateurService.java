package com.ecommerce.services;

import com.ecommerce.dto.UtilisateurRequestDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.Role;
import com.ecommerce.entities.TypeRole;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.mapper.GetAllCategorieDtoMapper;
import com.ecommerce.mapper.GetAllUtilisateurDtoMapper;
import com.ecommerce.mapper.UtilisateurDtoMapper;
import com.ecommerce.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurDtoMapper utilisateurDtoMapper;
  private final GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurDtoMapper utilisateurDtoMapper, GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurDtoMapper = utilisateurDtoMapper;

        this.getAllUtilisateurDtoMapper = getAllUtilisateurDtoMapper;
    }

    // ENREGISTREMENT D'UN UTILISATEUR EN BASE DE DONNEE
    public UtilisateurResponseDto createUtilisateur(UtilisateurRequestDto utilisateurRequestDto){
         // JE VERIFIE SI CETTE ADRESSE EMAIL EST DEJA UTILISEE

      Optional<Utilisateur> utilisateur = this.utilisateurRepository.findUtilisateurByEmail(
                utilisateurRequestDto.email()
        );
      if(utilisateur.isPresent()){
          throw  new EntityNotFoundException("cette adresse Email Existe DEJA");
      }

        // JE DEFINIE LE ROLE DE MON UTILISATEUR
     Role role = new Role(TypeRole.UTILISATEUR);

     // CONSTRUCTION DE MON UTILISATEUR AVANT SAUVEGARDE EN BD
       Utilisateur utilisateurBd = Utilisateur.builder()
               .email(utilisateurRequestDto.email())
               .motDePasse(utilisateurRequestDto.motDePasse())
               .nom(utilisateurRequestDto.nom())
               .prenom(utilisateurRequestDto.prenom())
               .role(role)
               .build();
       //SAUVEGARDE DE MON UTILISATEUR EN BD
         Utilisateur utilisateurSauvegarde=this.utilisateurRepository.save(utilisateurBd);
         return utilisateurDtoMapper.apply(utilisateurSauvegarde);

    }

    // RECUPERATION DE TOUS LES UTILISATEURS

    public List<UtilisateurResponseDto> getAllUtilisateur(){
        List<Utilisateur> utilisateurs= this.utilisateurRepository.findAll();

        return getAllUtilisateurDtoMapper.apply(utilisateurs);
    }

    //RECUPERATION D'UN UTILISATEUR

    public UtilisateurResponseDto getUtilisateur(int utilisateur_id){
        Utilisateur utilisateur = this.utilisateurRepository.findById(utilisateur_id).orElseThrow(()->{
            throw new EntityNotFoundException(" cette utilisateur n'existe pas");
        });

        return utilisateurDtoMapper.apply(utilisateur);
    }

    // MISE A JOUR D'UN UTILISATEUR

    public UtilisateurResponseDto updateUtilisateur(int utilisateur_id, UtilisateurRequestDto utilisateurDto){
        Utilisateur utilisateurBd = this.utilisateurRepository.findById(utilisateur_id).orElseThrow(
                ()->new EntityNotFoundException("cet utilisateur n'existe pas!")
        );
        utilisateurBd.setEmail(utilisateurDto.email());
        utilisateurBd.setNom(utilisateurDto.nom());
        utilisateurBd.setPrenom(utilisateurDto.prenom());
        Utilisateur utilisateurSauvegarde = this.utilisateurRepository.save(utilisateurBd);

        return utilisateurDtoMapper.apply(utilisateurSauvegarde);
    }

    //SUPPRIMER UN UTILISATEUR
    public void deleteUtilisateur(int utilisateur_id){
        Utilisateur utilisateurBd = this.utilisateurRepository.findById(utilisateur_id).orElseThrow(
                ()->new EntityNotFoundException("cet utilisateur n'existe pas!")
        );
        this.utilisateurRepository.delete(utilisateurBd);
    }
}

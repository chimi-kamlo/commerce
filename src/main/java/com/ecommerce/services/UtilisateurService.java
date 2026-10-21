package com.ecommerce.services;

import com.ecommerce.dto.UtilisateurRequestDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.Role;
import com.ecommerce.entities.TypeRole;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.mapper.GetAllUtilisateurDtoMapper;
import com.ecommerce.mapper.UtilisateurDtoMapper;
import com.ecommerce.repositories.RoleRpository;
import com.ecommerce.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurDtoMapper utilisateurDtoMapper;
  private final GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper;
  private final PasswordEncoder passwordEncoder;
  private  final RoleRpository roleRpository;
    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurDtoMapper utilisateurDtoMapper, GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper, PasswordEncoder passwordEncoder, RoleRpository roleRpository) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurDtoMapper = utilisateurDtoMapper;

        this.getAllUtilisateurDtoMapper = getAllUtilisateurDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRpository = roleRpository;
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
        // CONSTRUCTION DE MON UTILISATEUR AVANT SAUVEGARDE EN BD
        String motDePasseEncode =   passwordEncoder.encode(utilisateurRequestDto.motDePasse());
        Utilisateur utilisateurBd = new Utilisateur(utilisateurRequestDto.nom(),
                utilisateurRequestDto.prenom(),
                utilisateurRequestDto.email(),
                motDePasseEncode);


        // JE DEFINIE LE ROLE DE MON UTILISATEUR

      Optional<Role> roleBd =this.roleRpository.findRoleByLibele(TypeRole.UTILISATEUR);

        if(roleBd.isPresent()){
            utilisateurBd.setRole(roleBd.get());

        }
         Role role = new Role();
         log.info(roleBd.isPresent() + "");
         role.setLibele(TypeRole.UTILISATEUR);
         utilisateurBd.setRole(role);




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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user= this.utilisateurRepository.findUtilisateurByEmail(username).orElseThrow(()->new UsernameNotFoundException("utilisateur introuvable"));

        return user;
    }
}

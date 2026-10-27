package com.ecommerce.services;

import com.ecommerce.dto.ProfilRequestDto;
import com.ecommerce.dto.UtilisateurRequestDto;
import com.ecommerce.dto.UtilisateurResponseDto;
import com.ecommerce.entities.*;
import com.ecommerce.mapper.GetAllUtilisateurDtoMapper;
import com.ecommerce.mapper.UtilisateurDtoMapper;
import com.ecommerce.repositories.ProfilRepository;
import com.ecommerce.repositories.RoleRpository;
import com.ecommerce.repositories.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UtilisateurService implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    private final UtilisateurDtoMapper utilisateurDtoMapper;
  private final GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper;
  private final PasswordEncoder passwordEncoder;
  private  final RoleRpository roleRpository;
  private final ProfilRepository profilRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository, UtilisateurDtoMapper utilisateurDtoMapper, GetAllUtilisateurDtoMapper getAllUtilisateurDtoMapper, PasswordEncoder passwordEncoder, RoleRpository roleRpository, ProfilRepository profilRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurDtoMapper = utilisateurDtoMapper;

        this.getAllUtilisateurDtoMapper = getAllUtilisateurDtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleRpository = roleRpository;
        this.profilRepository = profilRepository;
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
// RECHERCHER UN UTILISATEUR EN FONCTION DE SON USERNAME
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user= this.utilisateurRepository.findUtilisateurByEmail(username).orElseThrow(()->new UsernameNotFoundException("utilisateur introuvable"));

        return user;
    }

    // AJOUTER UN PROFIL A L'UTILISATEUR

    public void addUtilisateurProfil(MultipartFile image, ProfilRequestDto description) throws IOException {
       // TAILLE MAX DE L'IMAGE
        long tailleMax = 2 * 1024 * 1024; // 2 Mo
        // JE RECUPER L'UTILISATEUR CONNECTE
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(!(utilisateur !=null)){ throw new RuntimeException("utilisateur introuvable ! connectez-vous");}

       // CONTROLE LA TAILLE DU FICHIER
        if(image.getSize()>tailleMax){
            throw new IllegalArgumentException("La taille du fichier ne doit pas dépasser 2 Mo");
        }

      //VERIFIE SI LE FICHIER EST VIDE
        if(image.isEmpty()){
            throw new IllegalArgumentException("le fichier est vide");
        }


        Path path = Paths.get("images")
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(path);
        // RECUPERER LE TYPE DU FICHIER
        String type = image.getContentType();

        String extension = switch (type == null ? "" : type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException(
                    "Format de fichier non autorisé");
        };

        // CONSTRUCTION DU PATH DE MON FICHIER
        String nomFichier = UUID.randomUUID().toString()+ extension;

        Profil profil = new Profil(description.description(),nomFichier,utilisateur);
        utilisateur.setProfil(profil);
        this.profilRepository.save(profil);
        this.utilisateurRepository.save(utilisateur);

        Path destination = path.resolve(nomFichier)

                .normalize();
        image.transferTo(destination);

    }

    //RECUPER LE PROFIL DE L'UTILISATEUR CONNECTE


    public Map<String,String> getProfilUtilisateur() throws IOException {
    Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(utilisateur==null){
           throw new RuntimeException("utilisateur introuvable");
       }
        Path  path = Paths.get("images")
                .toAbsolutePath()
                .normalize();
    Profil profilUtilisateur = this.profilRepository.findById(utilisateur.getProfil().getId()).orElseThrow(
            ()->new EntityNotFoundException("le profil n'existe pas!")
    );

        Path chemin = path.resolve(profilUtilisateur.getAvatar());

          Map<String,String> profil = Map.of(
                  "description",utilisateur.getProfil().getDescription(),
                  "image_url","http://localhost:8080/api/images/" + profilUtilisateur.getAvatar()
          );


        return profil;
    }

     // RECUPERER L'IMAGE DE PROFIL D'UN UTILISATEUR

    public Resource getUtilisateurImage() throws IOException {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(utilisateur==null){
            throw new RuntimeException("utilisateur introuvable");
        }
        Path  path = Paths.get("images",utilisateur.getProfil().getAvatar())
                .toAbsolutePath()
                .normalize();

        if (!Files.isRegularFile(path)||!Files.exists(path)) {
            throw new IllegalArgumentException("le fichier est vide");
        }

        Resource resource = new UrlResource(path.toUri());
        return resource;
    }


    // récupérer le path d'une image
    public Path getUtilisateurImagePath() throws IOException {
        Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(utilisateur==null){
            throw new RuntimeException("utilisateur introuvable");
        }
        Path dossier = Paths.get("images")
                .toAbsolutePath()
                .normalize();

        // concatenation du nom de l'image du produit en BD avec le chemin du dossier dans lequel elle se trouve en local
        Path chemin = dossier.resolve(utilisateur.getProfil().getAvatar());

        if (!Files.isRegularFile(chemin)) {
            throw new RuntimeException("Image irreguliere");
        }

        return chemin;
    }

}

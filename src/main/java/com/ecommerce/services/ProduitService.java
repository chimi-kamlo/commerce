package com.ecommerce.services;

import com.ecommerce.dto.ProduitRequestDto;
import com.ecommerce.dto.ProduitResponseDto;
import com.ecommerce.entities.Categorie;
import com.ecommerce.entities.Produit;
import com.ecommerce.errors.RessourceIntrouvableException;
import com.ecommerce.mapper.GetAllProduitDtoMapper;
import com.ecommerce.mapper.ProduitDtoMapper;
import com.ecommerce.repositories.CategorieRepository;
import com.ecommerce.repositories.ProduitRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProduitService {
    private  final ProduitRepository produitRepository;
    private  final ProduitDtoMapper produitDtoMapper;
    private final CategorieRepository categorieRepository;
    private  final GetAllProduitDtoMapper getAllProduitDtoMapper;


    public ProduitService(ProduitRepository produitRepository, ProduitDtoMapper produitDtoMapper, CategorieRepository categorieRepository, GetAllProduitDtoMapper getAllProduitDtoMapper) {
        this.produitRepository = produitRepository;
        this.produitDtoMapper = produitDtoMapper;
        this.categorieRepository = categorieRepository;

        this.getAllProduitDtoMapper = getAllProduitDtoMapper;
    }
//CREATION D'UN PRODUIT
    public ProduitResponseDto createroduit(ProduitRequestDto produit){
        Categorie categorie = this.categorieRepository.findCategorieByCodeCategorie(produit.codeCategorie())
                .orElseThrow(()->new IllegalArgumentException("categorie introuvable"));
         Produit produits = Produit.builder()
                 .nomProduit(produit.nomProduit())
                 .prix(produit.prix())
                 .stock(produit.stock())
                 .categorie(categorie)
                 .build();

         Produit produitSauvegardee =produitRepository.save(produits);
        return produitDtoMapper.apply(produitSauvegardee);
    }

    // RECUPERATION DE TOUS LES PRODUITS

    public List<ProduitResponseDto> getAllProduit(){
        List<Produit> produits = this.produitRepository.findAll();

        return getAllProduitDtoMapper.apply(produits);
    }

    //RECUPERATION D'UN PRODUIT

    public ProduitResponseDto getProduit(int id){
       Produit  produit = this.produitRepository.findById(id).orElseThrow(()->new EntityNotFoundException("produit introuvable"));

        return produitDtoMapper.apply(produit);
    }


    // METTRE UN PRODUIT A JOUR

    public ProduitResponseDto updateProduit(int id,ProduitRequestDto produitrequest){
        Categorie categorie=null;
        if(produitrequest.codeCategorie()!=null){
             categorie = this.categorieRepository.findCategorieByCodeCategorie(produitrequest.codeCategorie())
                    .orElseThrow(()->new IllegalArgumentException("categorie introuvable"));
        }
        Produit  produit = this.produitRepository.findById(id).orElseThrow(()->new EntityNotFoundException("produit introuvable"));
             produit.setCategorie(categorie);
             produit.setNomProduit(produitrequest.nomProduit());
             produit.setPrix(produitrequest.prix());
             produit.setStock(produitrequest.stock());
             Produit produitSauvegardee = this.produitRepository.save(produit);
        return produitDtoMapper.apply(produitSauvegardee);
    }

    // SUPPRESSION D'UN PRODUIT

    public void delecteProduit(int id){
        Produit  produit = this.produitRepository.findById(id).orElseThrow(()->new EntityNotFoundException("produit introuvable"));
        this.produitRepository.delete(produit);
    }

    //AJOUTER UNE IMAGE A UN PRODUIT

    public String addFile( int produit_id , MultipartFile fichier) throws IOException {
    Produit produit = this.produitRepository.getProduitById(produit_id).orElseThrow(() -> {return new EntityNotFoundException("produit introvable");});
        long tailleMax = 2 * 1024 * 1024; // 2 Mo

        if(fichier.getSize()>tailleMax){
            throw new IllegalArgumentException("La taille du fichier ne doit pas dépasser 2 Mo");
        }
      if(fichier.isEmpty()){
          throw new RessourceIntrouvableException("le fichier est vide");
      }
        Path dossier = Paths.get("uploads")
                        .toAbsolutePath()
                .normalize();

        Files.createDirectories(dossier);

        // RECUPERER LE TYPE DU FICHIER
        String type = fichier.getContentType();

        String extension = switch (type == null ? "" : type) {
            case "image/jpeg" -> ".jpg";
            case "image/png" -> ".png";
            case "image/webp" -> ".webp";
            default -> throw new IllegalArgumentException(
                    "Format de fichier non autorisé");
        };

        // CONSTRUCTION DU PATH DE MON FICHIER
        String nomFichier = UUID.randomUUID().toString()+ extension;

       // insertion de l'image
        produit.setImageProduit(nomFichier);
        this.produitRepository.save(produit);

        // CONSTRUCTION DU PATH DE DESTINATION DE MON FICHIER
        Path destination = dossier.resolve(nomFichier)

                .normalize();

        fichier.transferTo(destination);


        return "succes";
    }
    // RECUPERER L'IMAGE D'UN PRODUIT

    public Resource getProduitImage(int produit_id) throws IOException {
        Produit produit = this.produitRepository.getProduitById(produit_id).orElseThrow(() -> {return new EntityNotFoundException("produit introvable");});
      Path  path = Paths.get("uploads",produit.getImageProduit())
                .toAbsolutePath()
                .normalize();

        if (!Files.isRegularFile(path)||!Files.exists(path)) {
            throw new IllegalArgumentException("le fichier est vide");
        }



      Resource resource = new UrlResource(path.toUri());
        return resource;
    }

    // récupérer la path d'une image
    public Path getProduitImagePath(int produitId)
            throws IOException {

        Path dossier = Paths.get("uploads")
                .toAbsolutePath()
                .normalize();

        // récupérer le nom du fichier depuis le produit

        Produit produit = produitRepository.findById(produitId)
                .orElseThrow(() ->
                        new RuntimeException("Produit introuvable"));

      // concatenation du nom de l'image du produit en BD avec le chemin du dossier dans lequel elle se trouve en local
        Path chemin = dossier.resolve(produit.getImageProduit());

        if (!Files.isRegularFile(chemin)) {
            throw new RuntimeException("Image introuvable");
        }

        return chemin;
    }




    


   


}

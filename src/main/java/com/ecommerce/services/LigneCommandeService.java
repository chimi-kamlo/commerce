package com.ecommerce.services;

import com.ecommerce.repositories.LigneCommandeRepository;
import org.springframework.stereotype.Service;

@Service
public class LigneCommandeService {
    private  final LigneCommandeRepository ligneCommandeRepository;

    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }


}

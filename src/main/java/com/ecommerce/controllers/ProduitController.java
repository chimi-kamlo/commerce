package com.ecommerce.controllers;

import com.ecommerce.services.ProduitService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProduitController {
    private final ProduitService produitService;

    public ProduitController(ProduitService produitService) {
        this.produitService = produitService;
    }
}

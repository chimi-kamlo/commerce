package com.ecommerce.errors;

public class RessourceIntrouvableException extends RuntimeException{
    public RessourceIntrouvableException(String message) {
        super(message);
    }
}

package com.ecommerce.security;

import com.ecommerce.dto.AuthenticationDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthenticationService {

    public  final AuthenticationManager authenticationManager;
    public final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }
 // AUTHENTIFICATION D'UN UTILISATEUR
    public Map<String,String> Authentification(AuthenticationDto authenticationDto){
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationDto.username(), authenticationDto.password()));
       if(!authenticate.isAuthenticated()){
           throw new BadCredentialsException("mot de passe ou email incorrect");
       }
     return this.jwtService.generateToken(authenticationDto.username());
    }
}

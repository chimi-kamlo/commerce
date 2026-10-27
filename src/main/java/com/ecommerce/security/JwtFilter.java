package com.ecommerce.security;

import com.ecommerce.entities.Jwt;
import com.ecommerce.entities.Utilisateur;
import com.ecommerce.services.UtilisateurService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
@Slf4j
@Service
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
   private final UtilisateurService utilisateurService;
   private final HandlerExceptionResolver handlerExceptionResolver;
    public JwtFilter(JwtService jwtService, UtilisateurService utilisateurService, HandlerExceptionResolver handlerExceptionResolver) {
        this.jwtService = jwtService;
        this.utilisateurService = utilisateurService;

        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        boolean isExpire = true;
        String username = null;
        Jwt jwtBd;
        String authorization = request.getHeader("Authorization");

        if(authorization==null||!authorization.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        try {


         jwt = authorization.substring(7);
        isExpire = this.jwtService.IsExpired(jwt);
        username = this.jwtService.extractUsername(jwt);

        jwtBd = this.jwtService.getBdToken(jwt);

        if(!isExpire &&jwtBd.getUtilisateur().getUsername().equals(username)&& SecurityContextHolder.getContext().getAuthentication()==null) {
            Utilisateur utilisateur = (Utilisateur) this.utilisateurService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticate = new UsernamePasswordAuthenticationToken(utilisateur, null, utilisateur.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        }
        filterChain.doFilter(request,response);
    } catch (Exception exception) {
           handlerExceptionResolver.resolveException(request,response,null,exception);
        }
    }

}


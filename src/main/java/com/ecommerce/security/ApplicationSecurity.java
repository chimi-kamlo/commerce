package com.ecommerce.security;

import com.ecommerce.entities.Utilisateur;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class ApplicationSecurity {
private final UserDetailsService userDetailsService;
private  final BCryptPasswordEncoder bCryptPasswordEncoder;
private  final JwtFilter jwtFilter;
private final CustomAccessDeniedHandler accessDeniedHandler;

    public ApplicationSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter, CustomAccessDeniedHandler accessDeniedHandler) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter = jwtFilter;
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   HandlerExceptionResolver handlerExceptionResolver){

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception
                .accessDeniedHandler(accessDeniedHandler)
        )
                .authorizeHttpRequests(htt->
                        htt.requestMatchers(HttpMethod.POST,"/utilisateurs/inscription").permitAll()
                                .requestMatchers(HttpMethod.POST,"/utilisateurs/Authentication").permitAll()

                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()
                                // Gestion des produits
                                .requestMatchers(HttpMethod.POST, "/produits/**")
                                .hasAuthority("PRODUIT_CREATE")

                                .requestMatchers(HttpMethod.GET, "/produits/**")
                                .hasAuthority("PRODUIT_READ")

                                .requestMatchers(HttpMethod.PUT, "/produits/**")
                                .hasAuthority("PRODUIT_UPDATE")

                                .requestMatchers(HttpMethod.DELETE, "/produits/**")
                                .hasAuthority("PRODUIT_DELETE")
                                //Gestion des commandes
                                .requestMatchers(HttpMethod.POST, "/commandes/**")
                                .hasAuthority("PASSER_COMMANDE")

                                .requestMatchers(HttpMethod.GET, "/commandes/**")
                                .hasAuthority("COMMANDE_READ")

                                .requestMatchers(HttpMethod.PATCH, "/commandes/**")
                                .hasAuthority("COMMANDE_UPDATE")
                                //Gestion des commandes
                                .requestMatchers(HttpMethod.POST, "/categories/**")
                                .hasAuthority("CATEGORIE_CREATE")

                                .requestMatchers(HttpMethod.GET, "/categories/**")
                                .hasAuthority(" CATEGORIE_READ")
                                //Gestion des commandes
                                .requestMatchers(HttpMethod.GET, "/utilisateurs/**")
                                .hasAuthority(" UTILISATEUR_READ")

                                .requestMatchers(HttpMethod.DELETE, "/utilisateurs/**")
                                .hasAuthority("UTILISATEUR_DELETE")
                                .anyRequest().authenticated()
                )

                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)  ;

        return httpSecurity.build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(this.userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}

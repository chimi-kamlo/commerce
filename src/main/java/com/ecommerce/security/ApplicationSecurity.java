package com.ecommerce.security;

import com.ecommerce.entities.Utilisateur;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
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
@EnableWebSecurity
public class ApplicationSecurity {
private final UserDetailsService userDetailsService;
private  final BCryptPasswordEncoder bCryptPasswordEncoder;
private  final JwtFilter jwtFilter;

    public ApplicationSecurity(UserDetailsService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilter jwtFilter) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilter = jwtFilter;
    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   HandlerExceptionResolver handlerExceptionResolver){

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->
                        exception.authenticationEntryPoint((request, response, authException)->{
                            handlerExceptionResolver.resolveException(request,response,null,authException);
                        })
                        )
                .authorizeHttpRequests(htt->
                        htt.requestMatchers(HttpMethod.POST,"/utilisateurs/inscription").permitAll()
                                .requestMatchers(HttpMethod.POST,"/utilisateurs/Authentication").permitAll()
                                .requestMatchers(
                                        "/v3/api-docs/**",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html"
                                ).permitAll()
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

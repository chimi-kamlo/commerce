package com.ecommerce.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(info = @Info(

        contact = @Contact(
                name = "chimi kamlo",
                email = "romaricchimi355@gmail.com",
                url = "https://github.com/chimi-kamlo"

        ),
        description = "documentation de mon backend E-commerce",
        title = "Api-Ecommerce",
        version = "1.0"),
        servers = {
               @Server(
                       description = "Environnement Local",
                       url = "http://localhost:8080/api"
               )

        }




)
@SecurityScheme(
        name = "bearerAuth",
        description = "Jwt Auth description",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfiguration {
}

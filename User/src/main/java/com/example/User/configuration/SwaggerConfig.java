package com.example.User.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Microsserviço de Usuário",
                description = "Lida com operações relacionadas ao cadastro e autenticação de usuários.",
                version = "v1"
        ),
        servers = @Server(url = "http://localhost:8082/api/user")
)
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI();
    }
}




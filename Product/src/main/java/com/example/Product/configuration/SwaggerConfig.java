package com.example.Product.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
        title = "Microsserviço de Produto",
        description = "Responsável por gerenciar operações relacionadas a produtos, como adição, remoção, atualização e busca de produtos.",
        version = "v1",
        license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
))
public class SwaggerConfig {

}

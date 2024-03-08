package com.example.Shopping_Cart.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info=@Info(title="Microsserviço de Carrinho de Compras", description = "Responsável por gerenciar o carrinho de compras dos usuários, permitindo adição e remoção de produtos.", version = "v1", license = @License(name="MIT", url="http:localhost")))
public class SwaggerConfig {


}

package com.example.Payment.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient userWebClient(WebClient.Builder builder){
        return builder.baseUrl("http://localhost:8082").build();
    }

    @Bean
    public WebClient shoppingCartWebClient(WebClient.Builder builder){
        return builder.baseUrl("http://localhost:8081").build();
    }
}

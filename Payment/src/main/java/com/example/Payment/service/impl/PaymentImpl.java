package com.example.Payment.service.impl;

import com.example.Payment.model.Payment;
import com.example.Payment.model.ShoppingCartDetailsDTO;
import com.example.Payment.repository.PaymentRepository;
import com.example.Payment.service.PaymentService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PaymentImpl implements PaymentService {

    @Autowired
    private WebClient userWebClient; // Injeta o WebClient configurado para o serviço de usuário

    @Autowired
    private WebClient shoppingCartWebClient;

    @Autowired
    private PaymentRepository paymentRepository;

    // Injeção do AmqpTemplate
    @Autowired
    private AmqpTemplate rabbitTemplate;

    @Override
    public List<Payment> getAll() {

        return paymentRepository.findAll();

    }

    private Boolean LoggedIn(String token) {
        try {
            Boolean isLoggedIn = this.userWebClient
                    .get()
                    .uri("/api/user/isLoggedIn")
                    .headers(headers -> headers.setBearerAuth(token.replace("Bearer ", "")))
                    .exchangeToMono(clientResponse -> {
                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return Mono.just(true);
                        } else {
                            return Mono.just(false);
                        }
                    })
                    .block();

            return isLoggedIn != null && isLoggedIn;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao verificar autenticação do usuário: " + e.getMessage(), e);
        }
    }

    private ShoppingCartDetailsDTO deletingItemFromShoppingCart(Long shoppingCartId, String token) {
        try {
            return this.shoppingCartWebClient.delete()
                    .uri("/shopping-cart/delete/{shoppingCartId}", shoppingCartId)
                    .headers(headers -> headers.setBearerAuth(token.replace("Bearer ", "")))
                    .retrieve()
                    .bodyToMono(ShoppingCartDetailsDTO.class)
                    .block();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao excluir item do carrinho de compras: " + e.getMessage(), e);
        }
    }

    @Override
    public Payment payment(Payment payment, String token, Long shoppingCartId) {

        try {
            try {
                Boolean isLoggedIn = LoggedIn(token);
                if (isLoggedIn != null && !isLoggedIn) {
                    throw new IllegalArgumentException("Usuário não autenticado");
                }
            } catch (WebClientResponseException ex) {
                if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                    throw new IllegalArgumentException("Usuário não autenticado");
                } else {
                    throw new RuntimeException("Erro ao verificar autenticação do usuário: " + ex.getMessage(), ex);
                }
            }

            // Excluindo item do carrinho de compras
            ShoppingCartDetailsDTO cartDetails;
            try {
                cartDetails = deletingItemFromShoppingCart(shoppingCartId, token);
                if (cartDetails == null) {
                    throw new IllegalArgumentException("Erro ao excluir item do carrinho de compras");
                }
                // Verificando se o código de status é 200 OK
                if (cartDetails.getStatusCode() == HttpStatus.OK.value()) {
                    // Mensagem de sucesso
                    System.out.println("Item do carrinho de compras excluído com sucesso!");
                } else {
                    // Lidar com outros códigos de status, se necessário
                    // Por exemplo, lançar uma exceção se o código de status não for 200 OK
                    throw new RuntimeException("Erro ao excluir item do carrinho de compras. Código de status: " + cartDetails.getStatusCode());
                }
            } catch (WebClientResponseException ex) {
                if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new IllegalArgumentException("Item do carrinho de compras não encontrado");
                } else {
                    throw new RuntimeException("Erro ao excluir item do carrinho de compras: " + ex.getMessage(), ex);
                }
            }


            return paymentRepository.save(payment);


        }  catch (Exception e) {
            if (e.getMessage().contains("200 OK from DELETE")) {

                // Envio da mensagem para a fila do RabbitMQ
                rabbitTemplate.convertAndSend("fila-pagamento", "Pagamento realizado com sucesso!");

                return paymentRepository.save(payment);

            }
            else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }


    }

}


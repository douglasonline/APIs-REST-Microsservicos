package com.example.Shopping_Cart.service.impl;

import com.example.Shopping_Cart.model.CartItem;
import com.example.Shopping_Cart.model.ProductDetailsDTO;
import com.example.Shopping_Cart.model.ShoppingCart;
import com.example.Shopping_Cart.repository.CartItemRepository;
import com.example.Shopping_Cart.repository.ShoppingCartRepository;
import com.example.Shopping_Cart.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ShoppingCartImpl implements ShoppingCartService {

    @Autowired
    private WebClient productWebClient;

    @Autowired
    ShoppingCartRepository shoppingCartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private WebClient userWebClient; // Injeta o WebClient configurado para o serviço de usuário


    private ProductDetailsDTO getProductDetails(Long productId) {
        return this.productWebClient.get()
                .uri("/product/{id}", productId)
                .retrieve()
                .bodyToMono(ProductDetailsDTO.class)
                .block();
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


    @Override
    public List<ShoppingCart> getAll() {

        return shoppingCartRepository.findAll();

    }


    @Override
    public ShoppingCart create(ShoppingCart shoppingCart, String token) {
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

            // Verifica se o carrinho está vazio
            if (shoppingCart.getItems() == null || shoppingCart.getItems().isEmpty()) {
                throw new IllegalArgumentException("O carrinho está vazio. Adicione itens ao carrinho.");
            }

            BigDecimal totalPrice = BigDecimal.ZERO;

            // Itera sobre os itens do carrinho
            for (CartItem item : shoppingCart.getItems()) {
                // Verifica se o campo productId está presente em cada item
                if (item.getProductId() == null) {
                    throw new IllegalArgumentException("O campo productId é obrigatório em cada item do carrinho.");
                }

                // Verifica se o produto associado ao item existe
                ProductDetailsDTO productDetails;
                try {
                    // Verifica se o produto associado ao item existe
                    productDetails = getProductDetails(item.getProductId());
                    if (productDetails == null) {
                        throw new IllegalArgumentException("Produto não encontrado com o ID: " + item.getProductId());
                    }
                } catch (WebClientResponseException.NotFound ex) {
                    throw new IllegalArgumentException("Produto não encontrado com o ID: " + item.getProductId());
                }

                // Calcula o totalPrice do item multiplicando o preço unitário pelo quantity do item
                BigDecimal itemTotalPrice = productDetails.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

                // Define o preço individual do item
                item.setPrice(productDetails.getPrice());

                // Adiciona o totalPrice do item ao totalPrice total do carrinho
                totalPrice = totalPrice.add(itemTotalPrice);

                // Define o carrinho de compras no item
                item.setShoppingCart(shoppingCart);
            }

            // Define o totalPrice total do carrinho
            shoppingCart.setTotalPrice(totalPrice);

            // Salva o carrinho e seus itens no banco de dados
            shoppingCart = shoppingCartRepository.save(shoppingCart);

            // Retorna o carrinho atualizado
            return shoppingCart;

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(Long shoppingCartId, Long productId, String token) {
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

            // Busca o carrinho de compras com base no ID fornecido
            Optional<ShoppingCart> optionalShoppingCart = shoppingCartRepository.findById(shoppingCartId);
            if (optionalShoppingCart.isEmpty()) {
                throw new NoSuchElementException("Carrinho de compras não encontrado com o ID: " + shoppingCartId);
            }

            ShoppingCart shoppingCart = optionalShoppingCart.get();

            // Verifica se o produto está presente no carrinho de compras
            boolean productFound = shoppingCart.getItems().stream()
                    .anyMatch(item -> item.getProductId().equals(productId));
            if (!productFound) {
                throw new NoSuchElementException("Produto com ID " + productId + " não encontrado no carrinho de compras com ID " + shoppingCartId);
            }

            // Remove o item do carrinho de compras com base no ID do produto
            shoppingCart.getItems().removeIf(item -> item.getProductId().equals(productId));

            // Recalcula o preço total do carrinho de compras
            BigDecimal totalPrice = shoppingCart.getItems().stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            shoppingCart.setTotalPrice(totalPrice);

            // Salva as alterações no carrinho de compras
            return shoppingCartRepository.save(shoppingCart);

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    @Override
    public  void removeItemsFromShoppingCart(Long shoppingCartId, String token) {
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

            ShoppingCart existingProduct = shoppingCartRepository.findById(shoppingCartId).orElseThrow(() -> new NoSuchElementException("Carrinho de compras não encontrado com o ID: " + shoppingCartId));

            if (existingProduct != null)  {

                shoppingCartRepository.deleteById(existingProduct.getId());

            }


        } catch (NoSuchElementException e) {
           throw new NoSuchElementException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }



}

package com.example.Shopping_Cart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotação para ignorar a serialização do ShoppingCart ao serializar um CartItem
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "shopping_cart_id")
    private ShoppingCart shoppingCart;

    private Long productId;
    private int quantity;
    private BigDecimal price;
}


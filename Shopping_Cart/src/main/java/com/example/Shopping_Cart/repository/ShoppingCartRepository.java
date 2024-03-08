package com.example.Shopping_Cart.repository;

import com.example.Shopping_Cart.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    //Encontrar um item do ShoppingCart com base no ID do produto associado aos seus itens
    ShoppingCart findByItemsProductId(Long productId);

}

package com.example.Shopping_Cart.repository;

import com.example.Shopping_Cart.model.CartItem;
import com.example.Shopping_Cart.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {


}

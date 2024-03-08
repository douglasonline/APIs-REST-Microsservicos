package com.example.Shopping_Cart.service;

import com.example.Shopping_Cart.model.ShoppingCart;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ShoppingCartService  {

      List<ShoppingCart> getAll();
      ShoppingCart create(ShoppingCart shoppingCart, String token);

      ShoppingCart removeProductFromShoppingCart(Long shoppingCartId, Long productId, String token);

      void removeItemsFromShoppingCart(Long shoppingCartId, String token);

      ShoppingCart update(Long id, ShoppingCart shoppingCart);

}

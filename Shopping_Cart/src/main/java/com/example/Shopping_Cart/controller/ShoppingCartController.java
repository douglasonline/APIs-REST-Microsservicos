package com.example.Shopping_Cart.controller;

import com.example.Shopping_Cart.model.ShoppingCart;
import com.example.Shopping_Cart.service.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/shopping-cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        try {
            // Use o token conforme necessário
            List<ShoppingCart> products = shoppingCartService.getAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar os produtos: " + e.getMessage());
        }
    }

    @Operation(summary = "Update a shopping cart by ID")
    @PutMapping("/{id}")
    @ApiResponse(responseCode = "200", description = "Shopping Cart updated")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody ShoppingCart shoppingCart) {
        try {
            ShoppingCart shoppingCart1 = shoppingCartService.update(id, shoppingCart);
            return ResponseEntity.ok(shoppingCart1);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar produto: " + e.getMessage());
        }

    }


    @PostMapping("/addItem")
    public ResponseEntity<?> addItemToCart(@RequestBody ShoppingCart shoppingCart, @RequestHeader("Authorization") String token) {
        try {
            ShoppingCart updatedShoppingCart = shoppingCartService.create(shoppingCart, token);
            return ResponseEntity.ok(updatedShoppingCart);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @DeleteMapping("/{shoppingCartId}/items/{productId}")
    public ResponseEntity<?> removeProductFromShoppingCart(@PathVariable Long shoppingCartId, @PathVariable Long productId, @RequestHeader("Authorization") String token) {
        try {
            ShoppingCart updatedCart = shoppingCartService.removeProductFromShoppingCart(shoppingCartId, productId, token);
            return ResponseEntity.ok(updatedCart);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }


    @Operation(summary = "Delete a shopping cart by ID")
    @DeleteMapping("/delete/{shoppingCartId}")
    @ApiResponse(responseCode = "200", description = "Shopping Cart deleted")
    public ResponseEntity<?> delete(@PathVariable Long shoppingCartId, @RequestHeader("Authorization") String token) {
        try {
            shoppingCartService.removeItemsFromShoppingCart(shoppingCartId, token);
            return ResponseEntity.ok("Carrinho de compras com o ID " + shoppingCartId + " foi excluído com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar produto: " + e.getMessage());
        }
    }


}


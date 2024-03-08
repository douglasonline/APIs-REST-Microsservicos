package com.example.Product.controller;

import com.example.Product.model.Product;
import com.example.Product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
public class ProductController {

    @Autowired
    ProductService productService;

    @Operation(summary = "Get all products")
    @GetMapping("/product/all")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<?> getAll() {
        try {
            List<Product> products = productService.getAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar os produtos: " + e.getMessage());
        }
    }

    @Operation(summary = "Create a product")
    @PostMapping("/product")
    @ApiResponse(responseCode = "200", description = "Product created")
    public ResponseEntity<?> create(@RequestBody Product product) {
        try {
            Product createdProduct = productService.create(product);
            return ResponseEntity.ok(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao criar o produto: " + e.getMessage());
        }
    }

    @Operation(summary = "Get a product by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the product"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        try {
            Optional<Product> product = productService.findById(id);
            return ResponseEntity.ok(product);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao buscar produto: " + e.getMessage());
        }

    }

    @Operation(summary = "Update a product by ID")
    @PutMapping("/product/{id}")
    @ApiResponse(responseCode = "200", description = "Product updated")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Product product) {
        try {
            Product product1 = productService.update(id, product);
            return ResponseEntity.ok(product1);
        } catch (NoSuchElementException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar produto: " + e.getMessage());
        }

    }

    @Operation(summary = "Delete a product by ID")
    @DeleteMapping("/product/{id}")
    @ApiResponse(responseCode = "200", description = "Product deleted")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            productService.delete(id);
            return ResponseEntity.ok("Produto com o ID " + id + " foi excluído com sucesso.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar produto: " + e.getMessage());
        }
    }


}







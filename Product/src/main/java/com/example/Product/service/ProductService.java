package com.example.Product.service;

import com.example.Product.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {

       List<Product> getAll();

       Product create(Product product);

       Optional<Product> findById(Long id);

       Product update(Long id, Product product);

       void delete(Long id);


}

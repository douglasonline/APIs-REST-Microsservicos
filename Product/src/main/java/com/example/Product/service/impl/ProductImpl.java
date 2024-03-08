package com.example.Product.service.impl;

import com.example.Product.model.Product;
import com.example.Product.repository.ProductRepository;
import com.example.Product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductImpl implements ProductService {

    @Autowired
    ProductRepository productRepository;

    @Override
    public List<Product> getAll() {

       return productRepository.findAll();

    }

    @Override
    public Product create(Product product) {

      return productRepository.save(product);


    }

    @Override
    public Optional<Product> findById(Long id) {

        Product product = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado com o ID: " + id));

        return Optional.of(product);


    }

    @Override
    public Product update(Long id, Product product) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado com o ID: " + id));

        if (product != null)  {

            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());
            existingProduct.setCategory(product.getCategory());

        }

        return productRepository.save(existingProduct);

    }


    @Override
    public void delete(Long id) {

        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Produto não encontrado com o ID: " + id));

        if (existingProduct != null)  {

            productRepository.deleteById(existingProduct.getId());

        }

    }


}

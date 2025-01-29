package com.dripify.product.service;

import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public List<Product> getFilteredProducts(@Nullable String category, @Nullable Gender gender) {
        return productRepository.findProducts(category, gender);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id);
    }
}


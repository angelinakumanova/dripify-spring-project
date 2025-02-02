package com.dripify.product.service;

import com.dripify.exception.DomainException;
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


    public List<Product> getFilteredProducts(List<String> categoryNames, Gender gender) {
        return productRepository.findProductsByCategoryNameInAndGenderIn(categoryNames, List.of(gender, Gender.UNISEX));
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product not found"));
    }
}


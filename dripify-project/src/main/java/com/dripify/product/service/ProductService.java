package com.dripify.product.service;

import com.dripify.exception.DomainException;
import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    // TODO: Handle non-existent gender
    public Page<Product> getFilteredProducts(String category, String gender, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Gender genderEnum = gender == null ? null : Gender.valueOf(gender.toUpperCase());

        return productRepository.findProductsByCategoryAndGender(category, genderEnum, pageable);
    }

    public Page<Product> getNewArrivalsProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllByOrderByCreatedOnDesc(pageable);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product not found"));
    }
}


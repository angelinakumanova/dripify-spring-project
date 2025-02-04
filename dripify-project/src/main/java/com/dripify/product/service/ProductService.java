package com.dripify.product.service;

import com.dripify.exception.DomainException;
import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Page<Product> getFilteredProducts(String category, List<Gender> genders, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findProductsByCategoryAndGender(category, genders, pageable);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product not found"));
    }
}


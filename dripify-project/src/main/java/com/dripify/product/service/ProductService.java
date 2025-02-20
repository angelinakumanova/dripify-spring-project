package com.dripify.product.service;

import com.dripify.exception.DomainException;
import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // TODO: Handle non-existent gender
    public Page<Product> getFilteredProducts(ProductFilter productFilter, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return productRepository.findProductsByFilters(productFilter, pageable);
    }

    public Page<Product> getNewArrivalsProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return productRepository.findAllByOrderByCreatedOnDesc(pageable);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product does not exist"));
    }
}


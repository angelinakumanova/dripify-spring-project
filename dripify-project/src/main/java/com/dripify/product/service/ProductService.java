package com.dripify.product.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.exception.DomainException;
import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    public Page<Product> getFilteredProducts(String gender, String categoryName, String subcategoryName,
                                             ProductFilter productFilter, int page) {
        Gender genderEnum = Gender.valueOf(gender.toUpperCase());

        Category category = subcategoryName == null ?
                categoryService.getByName(categoryName) :
                categoryService.getByNameAndParentCategory(subcategoryName, categoryName);

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        if (productFilter.getSortBy() != null) {
            String[] split = productFilter.getSortBy().split("-");
            String field = split[0];
            String order = split[1];

            Sort sort = Sort.by(field);

            if (order.equals("asc")) {
                sort = sort.ascending();
            } else {
                sort = sort.descending();
            }

            pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);
        }

        return productRepository.findByCategoryAndGenderAndFilters(category,
                genderEnum,
                productFilter.getBrands(), productFilter.getBrands().size(),
                productFilter.getColors(), productFilter.getColors().size(),
                productFilter.getMaterials(), productFilter.getMaterials().size(),
                productFilter.getSizes(), productFilter.getSizes().size(),
                pageable);

    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product does not exist"));
    }


}


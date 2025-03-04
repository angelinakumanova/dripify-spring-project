package com.dripify.product.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.exception.DomainException;
import com.dripify.product.model.Product;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import com.dripify.web.dto.ProductFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, UserService userService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    public Page<Product> getFilteredProducts(String gender, String categoryName, String subcategoryName,
                                             ProductFilter productFilter, int page) {
        Gender genderEnum = Gender.valueOf(gender.toUpperCase());

        Category category = subcategoryName == null ?
                categoryService.getByName(categoryName) :
                categoryService.getByNameAndParentCategory(subcategoryName, categoryName);

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        //TODO: Implement Most Rated
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

    public void addNewProduct(CreateProductRequest createProductRequest, User user) {
        Product product = initializeProduct(createProductRequest, user);

        productRepository.save(product);
    }

    private static Product initializeProduct(CreateProductRequest createProductRequest, User user) {
        return Product.builder()
                .name(createProductRequest.getTitle())
                .description(createProductRequest.getDescription())
                .gender(createProductRequest.getGender())
                .category(createProductRequest.getCategory())
                .brand(createProductRequest.getBrand())
                .color(createProductRequest.getColor())
                .material(createProductRequest.getMaterial())
                .size(createProductRequest.getSize())
                .condition(createProductRequest.getCondition())
                .price(createProductRequest.getPrice())
                .seller(user)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }

    public Page<Product> getProductsByUsername(String username, int page) {
        User user = userService.getByUsername(username);

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return productRepository.getProductsBySeller(user, pageable);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductById(id).orElseThrow(() -> new DomainException("Product does not exist"));
    }


}


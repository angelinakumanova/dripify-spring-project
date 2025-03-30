package com.dripify.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.product.model.Product;
import com.dripify.product.model.enums.Brand;
import com.dripify.product.model.enums.Color;
import com.dripify.product.model.enums.Material;
import com.dripify.product.model.enums.Size;
import com.dripify.product.repository.ProductImageRepository;
import com.dripify.product.repository.ProductRepository;
import com.dripify.product.service.ProductService;
import com.dripify.review.model.Review;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.web.dto.ProductFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceUTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private CategoryService categoryService;

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @InjectMocks
    private ProductService productService;

    @Test
    void givenPriceDescSort_whenGetFilteredProducts_thenCallsRepositoryWithCorrectSortingParameter() {
        // Given
        String gender = "men";
        String categoryName = "clothing";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setSortBy("price-desc");
        int page = 0;

        when(categoryService.getByName(categoryName)).thenReturn(new Category(categoryName, Gender.UNISEX));

        // When
        productService.getFilteredProducts(gender, categoryName, null, productFilter, page);

        // Then
        verify(categoryService, times(1)).getByName(categoryName);
        verify(productRepository, times(1)).findByCategoryAndGenderAndFilters(
                any(Category.class),
                any(Gender.class),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                eq(""),
                eq(PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE, Sort.by("price").descending())));
    }

    @Test
    void givenPriceAscSort_whenGetFilteredProducts_thenCallsRepositoryWithCorrectSortingParameter() {
        // Given
        String gender = "men";
        String categoryName = "clothing";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setSortBy("price-asc");
        int page = 0;

        when(categoryService.getByName(categoryName)).thenReturn(new Category(categoryName, Gender.UNISEX));

        // When
        productService.getFilteredProducts(gender, categoryName, null, productFilter, page);

        // Then
        verify(categoryService, times(1)).getByName(categoryName);
        verify(productRepository, times(1)).findByCategoryAndGenderAndFilters(
                any(Category.class),
                any(Gender.class),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                eq(""),
                eq(PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE, Sort.by("price").ascending())));
    }

    @Test
    void givenMostRatedSort_whenGetFilteredProducts_thenCallsRepositoryWithCorrectSortingParameter() {
        // Given
        String gender = "men";
        String categoryName = "clothing";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setSortBy("mostRated");
        int page = 0;

        when(categoryService.getByName(categoryName)).thenReturn(new Category(categoryName, Gender.UNISEX));

        // When
        productService.getFilteredProducts(gender, categoryName, null, productFilter, page);

        // Then
        verify(categoryService, times(1)).getByName(categoryName);
        verify(productRepository, times(1)).findByCategoryAndGenderAndFilters(
                any(Category.class),
                any(Gender.class),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                eq("mostRated"),
                eq(PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE)));
    }

    @Test
    void givenNoSort_whenGetFilteredProducts_thenCallsRepositoryWithEmptySortingParameter() {
        // Given
        String gender = "men";
        String categoryName = "clothing";
        ProductFilter productFilter = new ProductFilter();
        productFilter.setSortBy(null);
        int page = 0;

        when(categoryService.getByName(categoryName)).thenReturn(new Category(categoryName, Gender.UNISEX));

        // When
        productService.getFilteredProducts(gender, categoryName, null, productFilter, page);

        // Then
        verify(categoryService, times(1)).getByName(categoryName);
        verify(productRepository, times(1)).findByCategoryAndGenderAndFilters(
                any(Category.class),
                any(Gender.class),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                eq(""),
                eq(PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE)));
    }

    @Test
    void givenInvalidGenderToSubcategoryGender_whenGetFilteredProducts_thenThrowsException() {
        // Given
        String gender = "men";
        String parentCategoryName = "clothing";
        String subcategoryName = "dresses";

        Gender genderEnum = Gender.WOMEN;
        Category parentCategory = new Category();
        parentCategory.setName(parentCategoryName);
        Category subcategory = new Category();
        subcategory.setName(subcategoryName);
        subcategory.setGender(genderEnum);
        subcategory.setParentCategory(parentCategory);

        when(categoryService.getByNameAndParentCategory(subcategoryName, parentCategoryName)).thenReturn(subcategory);

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> productService.getFilteredProducts(gender, parentCategoryName, subcategoryName, new ProductFilter(), 0));
        verify(categoryService, never()).getByName(anyString());
        verify(productRepository, never()).findByCategoryAndGenderAndFilters(any(Category.class), any(Gender.class),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyList(), anyInt(),
                anyString(), any(Pageable.class));
    }

    @Test
    void givenPageRequest_whenGetNewProducts_thenReturnsPageOfProducts() {
        // Given
        int page = 0;
        Pageable pageable = PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE);

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 1")
                .isActive(true)
                .createdOn(LocalDateTime.now())
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .name("Product 2")
                .isActive(true)
                .createdOn(LocalDateTime.now().minusDays(1))
                .build();

        List<Product> products = List.of(product1, product2);
        Page<Product> expectedPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.getAllByIsActiveTrueOrderByCreatedOnDesc(pageable)).thenReturn(expectedPage);

        // When
        Page<Product> result = productService.getNewProducts(page);

        // Then
        verify(productRepository, times(1)).getAllByIsActiveTrueOrderByCreatedOnDesc(pageable);
        assertEquals(2, result.getTotalElements());
        assertEquals("Product 1", result.getContent().get(0).getName());
        assertEquals("Product 2", result.getContent().get(1).getName());
        assertEquals(page, result.getNumber());
        assertEquals(ProductService.DEFAULT_PAGE_SIZE, result.getSize());
    }

    @Test
    void givenCurrentProduct_whenGetSimilarProducts_thenReturnsListOfSimilarProducts() {
        // Given
        int limit = 5;
        Product currentProduct = Product.builder()
                .id(UUID.randomUUID())
                .category(new Category("clothing", Gender.MEN))
                .gender(Gender.MEN)
                .build();

        Product similarProduct1 = Product.builder()
                .id(UUID.randomUUID())
                .category(currentProduct.getCategory())
                .gender(currentProduct.getGender())
                .build();
        Product similarProduct2 = Product.builder()
                .id(UUID.randomUUID())
                .category(currentProduct.getCategory())
                .gender(currentProduct.getGender())
                .build();

        List<Product> products = List.of(similarProduct1, similarProduct2);

        when(productRepository.getProductsByCategoryOrderByCreatedOnDesc(
                currentProduct.getCategory(),
                currentProduct.getGender(),
                currentProduct.getId(),
                limit
        )).thenReturn(products);

        // When
        List<Product> result = productService.getSimilarProducts(limit, currentProduct);

        // Then
        verify(productRepository, times(1)).getProductsByCategoryOrderByCreatedOnDesc(
                currentProduct.getCategory(),
                currentProduct.getGender(),
                currentProduct.getId(),
                limit
        );

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(similarProduct1));
        assertTrue(result.contains(similarProduct2));
    }

}

package com.dripify.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.product.event.ProductDeactivationEvent;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.model.enums.*;
import com.dripify.product.repository.ProductImageRepository;
import com.dripify.product.repository.ProductRepository;
import com.dripify.product.service.ProductService;
import com.dripify.review.model.Review;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.web.dto.CreateProductRequest;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.ProductFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    @Test
    void givenUserAndCurrentProduct_whenGetUserLatestProducts_thenReturnsListOfProductsExcludingCurrent() {
        // Given
        User user = User.builder()
                .id(UUID.randomUUID())
                .build();
        UUID currentProductId = UUID.randomUUID();

        Product product1 = Product.builder()
                .id(UUID.randomUUID())
                .seller(user)
                .isActive(true)
                .build();
        Product product2 = Product.builder()
                .id(UUID.randomUUID())
                .seller(user)
                .isActive(true)
                .build();
        Product product3 = Product.builder()
                .id(UUID.randomUUID())
                .seller(user)
                .isActive(true)
                .build();

        List<Product> products = List.of(product1, product2, product3);

        when(productRepository.getProductsBySellerAndIdNotAndIsActiveTrueOrderByCreatedOnDesc(
                eq(user), eq(currentProductId)
        )).thenReturn(products);

        // When
        List<Product> result = productService.getUserLatestProducts(user, currentProductId);

        // Then
        verify(productRepository, times(1)).getProductsBySellerAndIdNotAndIsActiveTrueOrderByCreatedOnDesc(
                eq(user), eq(currentProductId)
        );

        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.contains(product1));
        assertTrue(result.contains(product2));
        assertTrue(result.contains(product3));
        assertFalse(result.stream().anyMatch(p -> p.getId().equals(currentProductId)));
    }

    @Test
    void happyPath_whenAddNewProduct() {
        // Given
        CreateProductRequest createProductRequest = new CreateProductRequest();

        Category subcategory = new Category("Dresses", Gender.WOMEN);
        Category parentCategory = new Category("Clothing", Gender.UNISEX);
        subcategory.setParentCategory(parentCategory);

        createProductRequest.setCategory(subcategory);
        createProductRequest.setSize(Size.M);
        createProductRequest.setImages(List.of(mock(MultipartFile.class)));
        String imageUrl = "https://example.com/image.jpg";

        Product product = new Product();
        product.setId(UUID.randomUUID());
        when(productRepository.save(any(Product.class))).thenReturn(product);

        when(cloudinaryService.uploadProductImage(any(MultipartFile.class), anyString(), anyInt())).thenReturn(imageUrl);

        when(productImageRepository.save(any(ProductImage.class))).thenReturn(new ProductImage());

        // When
        productService.addNewProduct(createProductRequest, new User());

        // Then
        verify(productRepository, times(1)).save(any(Product.class));
        verify(cloudinaryService, times(1)).uploadProductImage(any(MultipartFile.class), anyString(), anyInt());
        verify(productImageRepository, times(1)).save(any(ProductImage.class));
        verify(productImageRepository).save(argThat(productImage1 -> productImage1.getImageUrl().equals(imageUrl)));
    }

    @Test
    void happyPath_whenEditProduct() {
        // Given
        User seller = new User();

        Category category = new Category("Heels", Gender.WOMEN);
        category.setParentCategory(new Category("Shoes", Gender.UNISEX));
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .category(category)
                .seller(seller)
                .name("Title")
                .description("Description")
                .brand(Brand.BALENCIAGA)
                .size(Size.EU36)
                .material(Material.LACE)
                .color(Color.BEIGE)
                .condition(Condition.ACCEPTABLE)
                .price(BigDecimal.valueOf(10))
                .build();

        ProductEditRequest productEditRequest = ProductEditRequest.builder()
                .title("New title")
                .description("New description")
                .brand(Brand.ADIDAS)
                .size(Size.EU38)
                .material(Material.FAUX_LEATHER)
                .color(Color.PINK)
                .condition(Condition.NEW)
                .price(BigDecimal.valueOf(20)).build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Product editedProduct = productService.editProduct(product, productEditRequest, seller);

        // Then
        assertEquals(editedProduct.getName(), productEditRequest.getTitle());
        assertEquals(editedProduct.getDescription(), productEditRequest.getDescription());
        assertEquals(editedProduct.getBrand(), productEditRequest.getBrand());
        assertEquals(editedProduct.getSize(), productEditRequest.getSize());
        assertEquals(editedProduct.getMaterial(), productEditRequest.getMaterial());
        assertEquals(editedProduct.getColor(), productEditRequest.getColor());
        assertEquals(editedProduct.getCondition(), productEditRequest.getCondition());
        assertEquals(editedProduct.getPrice(), productEditRequest.getPrice());
        verify(productRepository, times(1)).save(editedProduct);
    }

    @Test
    void givenUnauthorizedUser_whenEditProduct_thenThrowException() {
        // Given
        User unauthorizedUser = new User();
        Product product = Product.builder().seller(new User()).build();
        ProductEditRequest productEditRequest = ProductEditRequest.builder().build();

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> productService.editProduct(product, productEditRequest, unauthorizedUser));
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void happyPath_whenDeactivateProduct() {
        // Given
        User seller = new User();
        Product product = Product.builder().isActive(true).seller(seller).build();

        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        productService.deactivateProduct(product, seller);

        // Then
        assertFalse(product.isActive());
        verify(productRepository, times(1)).save(product);
        verify(eventPublisher, times(1)).publishEvent(any(ProductDeactivationEvent.class));

    }

    @Test
    void givenUnauthorizedUser_whenDeactivateProduct_thenThrowException() {
        // Given
        User unauthorizedUser = new User();
        Product product = Product.builder().isActive(true).seller(new User()).build();

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> productService.deactivateProduct(product, unauthorizedUser));
        verify(productRepository, never()).save(any(Product.class));
        verify(eventPublisher, never()).publishEvent(any(ProductDeactivationEvent.class));
    }

    @Test
    void givenUserAndPage_whenGetProductsByUsername_thenReturnsUserProducts() {
        // Given
        User user = new User();
        int page = 1;
        Pageable pageable = PageRequest.of(page, ProductService.DEFAULT_PAGE_SIZE);

        Product product1 = new Product();
        Product product2 = new Product();
        List<Product> products = List.of(product1, product2);
        Page<Product> expectedPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.getProductsBySellerAndIsActiveTrue(user, pageable)).thenReturn(expectedPage);

        // When
        Page<Product> result = productService.getProductsByUsername(user, page);

        // Then
        verify(productRepository, times(1)).getProductsBySellerAndIsActiveTrue(user, pageable);
        assertEquals(expectedPage, result);
        assertEquals(expectedPage.getTotalElements(), result.getTotalElements());
        assertEquals(page, result.getNumber());
        assertEquals(ProductService.DEFAULT_PAGE_SIZE, result.getSize());
    }

    @Test
    void givenExistingProductId_whenGetProductById_thenReturnsProduct() {
        // Given
        UUID productId = UUID.randomUUID();
        Product product = Product.builder().id(productId).build();

        when(productRepository.getProductByIdAndIsActiveTrue(productId)).thenReturn(Optional.of(product));

        // When
        Product fetchedProduct = productService.getProductById(productId);

        // Then
        assertEquals(product, fetchedProduct);
        verify(productRepository, times(1)).getProductByIdAndIsActiveTrue(productId);
    }

    @Test
    void givenNonExistingProductId_whenGetProductById_thenThrowException() {
        // Given
        UUID productId = UUID.randomUUID();
        when(productRepository.getProductByIdAndIsActiveTrue(productId)).thenReturn(Optional.empty());

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(productId));
    }


}

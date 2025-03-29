package com.dripify.product.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.product.event.ProductDeactivationEvent;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.model.enums.Size;
import com.dripify.product.model.enums.SizeCategory;
import com.dripify.product.repository.ProductImageRepository;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.user.event.UserDeactivationEvent;
import com.dripify.user.model.User;
import com.dripify.web.dto.*;
import jakarta.transaction.Transactional;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;

    private final ApplicationEventPublisher eventPublisher;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, CloudinaryService cloudinaryService, ProductImageRepository productImageRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.productImageRepository = productImageRepository;
        this.eventPublisher = eventPublisher;
    }

    public Page<Product> getFilteredProducts(String gender, String categoryName, String subcategoryName,
                                               ProductFilter productFilter, int page) {
        Gender genderEnum = Gender.valueOf(gender.toUpperCase());
        Category category = subcategoryName == null ?
                categoryService.getByName(categoryName) :
                categoryService.getByNameAndParentCategory(subcategoryName, categoryName);


        if (category.getParentCategory() != null) {
            validateGenderToCategory(category, genderEnum);
        }

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        String fieldSort = "";
        if (productFilter.getSortBy() != null && !productFilter.getSortBy().isEmpty()) {
            String[] split = productFilter.getSortBy().split("-");
            String field = split[0];

            if (field.equals("mostRated")) {
                fieldSort = field;

            } else {
                String order = split[1];

                Sort sort = Sort.by(field);

                if (order.equals("asc")) {
                    sort = sort.ascending();
                } else {
                    sort = sort.descending();
                }

                pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE, sort);
            }
        }

        return productRepository.findByCategoryAndGenderAndFilters(category,
                genderEnum,
                productFilter.getBrands(), productFilter.getBrands().size(),
                productFilter.getColors(), productFilter.getColors().size(),
                productFilter.getMaterials(), productFilter.getMaterials().size(),
                productFilter.getSizes(), productFilter.getSizes().size(),
                fieldSort,
                pageable);

    }

    public Page<Product> getNewProducts(int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        return productRepository.getAllByIsActiveTrueOrderByCreatedOnDesc(pageable);
    }



    public List<Product> getSimilarProducts(int limit, Product currentProduct) {

        return productRepository.getProductsByCategoryOrderByCreatedOnDesc(currentProduct.getCategory(), currentProduct.getGender(), currentProduct.getId(), limit)
                .stream()
                .toList();
    }

    public List<Product> getUserLatestProducts(User user, UUID currentProductId) {
        return productRepository.getProductsBySellerAndIdNotAndIsActiveTrueOrderByCreatedOnDesc(user, currentProductId);
    }

    @Transactional
    public void addNewProduct(CreateProductRequest createProductRequest, User user) {
        validateSize(createProductRequest.getCategory(), createProductRequest.getSize());

        Product product = initializeProduct(createProductRequest, user);
        productRepository.save(product);


        createProductRequest.getImages().forEach(image -> {
            String imageUrl = cloudinaryService.uploadProductImage(image, product.getId().toString(), createProductRequest.getImages().indexOf(image));

            ProductImage productImage = ProductImage.builder()
                    .imageUrl(imageUrl)
                    .product(product)
                    .build();

            productImageRepository.save(productImage);
        });
    }

    public void editProduct(Product product, ProductEditRequest productEditRequest, User user) {
        if (product.getSeller() != user) {
            throw new IllegalArgumentException("You are not allowed to edit this product.");
        }

        validateSize(product.getCategory(), productEditRequest.getSize());
        Product editedProduct = editProductData(product, productEditRequest);
        productRepository.save(editedProduct);
    }

    public void deactivateProduct(Product product, User user) {
        if (product.getSeller() != user) {
            throw new IllegalArgumentException("You are not allowed to deactivate this product.");
        }

        product.setActive(false);
        productRepository.save(product);

        eventPublisher.publishEvent(new ProductDeactivationEvent(product.getId()));
    }

    public Page<Product> getProductsByUsername(User user, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return productRepository.getProductsBySellerAndIsActiveTrue(user, pageable);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductByIdAndIsActiveTrue(id).orElseThrow(() -> new IllegalArgumentException("Product does not exist"));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void deactivateProductsByUser(UserDeactivationEvent event) {
        productRepository.deactivateUserProducts(event.getUser());
    }

    @Transactional
    public void deleteInactiveProducts() {
        List<UUID> inactiveProductsIds = productRepository.getIdsByIsActiveFalse();
        inactiveProductsIds.forEach(this::deleteProductImagesFromCloud);

        productImageRepository.deleteAllInactiveProductsImages();
        productRepository.deleteAllByIsActiveFalse();
    }

    private void deleteProductImagesFromCloud(UUID productId) {
        cloudinaryService.deleteProductImages(productId);
    }

    private void validateSize(Category category, Size size) {
        String parentCategory = category.getParentCategory().getName();
        SizeCategory sizeCategory = size != null ? size.getCategory() : null;

        if ("clothing".equalsIgnoreCase(parentCategory) && SizeCategory.SHOES.equals(sizeCategory)) {
            throw new IllegalArgumentException("Invalid clothing size: " + size);
        } else if ("shoes".equalsIgnoreCase(parentCategory) && SizeCategory.CLOTHING.equals(sizeCategory)) {
            throw new IllegalArgumentException("Invalid shoe size: " + size);
        } else if ("accessories".equalsIgnoreCase(parentCategory) && size != null) {
            throw new IllegalArgumentException("Product of type accessories must NOT have size.");
        }
    }

    private void validateGenderToCategory(Category category, Gender gender) {
        if ((category.getGender() != gender) && !category.getGender().equals(Gender.UNISEX) && !gender.equals(Gender.UNISEX)) {
            throw new IllegalArgumentException("This gender doesn't belong to the category");
        }
    }

    private Product editProductData(Product product, ProductEditRequest productEditRequest) {
        product.setName(productEditRequest.getTitle());
        product.setDescription(productEditRequest.getDescription());
        product.setPrice(productEditRequest.getPrice());
        product.setBrand(productEditRequest.getBrand());
        product.setColor(productEditRequest.getColor());
        product.setMaterial(productEditRequest.getMaterial());
        product.setSize(productEditRequest.getSize());
        product.setCondition(productEditRequest.getCondition());

        return product;
    }


    private Product initializeProduct(CreateProductRequest createProductRequest, User user) {


        return Product.builder()
                .isActive(true)
                .name(createProductRequest.getTitle())
                .description(createProductRequest.getDescription())
                .gender(createProductRequest.getGender())
                .category(createProductRequest.getCategory())
                .size(createProductRequest.getSize())
                .brand(createProductRequest.getBrand())
                .color(createProductRequest.getColor())
                .material(createProductRequest.getMaterial())
                .condition(createProductRequest.getCondition())
                .price(createProductRequest.getPrice())
                .seller(user)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();
    }



}


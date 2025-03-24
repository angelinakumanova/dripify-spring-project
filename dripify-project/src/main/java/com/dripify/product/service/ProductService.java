package com.dripify.product.service;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.cloudinary.service.CloudinaryService;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.repository.ProductImageRepository;
import com.dripify.product.repository.ProductRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.ProductFilter;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {
    private static final int DEFAULT_PAGE_SIZE = 20;

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ProductImageRepository productImageRepository;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, UserService userService, CloudinaryService cloudinaryService, ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.productImageRepository = productImageRepository;
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
        return productRepository.getProductsBySellerAndIdNotAndIsActiveOrderByCreatedOnDesc(user, currentProductId, true);
    }

    @Transactional
    public void addNewProduct(CreateProductRequest createProductRequest, User user) {
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

        Product editedProduct = editProductData(product, productEditRequest);
        productRepository.save(editedProduct);
    }

    public void deactivateProduct(Product product, User user) {
        if (product.getSeller() != user) {
            throw new IllegalArgumentException("You are not allowed to deactivate this product.");
        }

        product.setActive(false);
        productRepository.save(product);
    }

    public Page<Product> getProductsByUsername(String username, int page) {
        User user = userService.getByUsername(username);

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);

        return productRepository.getProductsBySellerAndIsActive(user, pageable, true);
    }


    public Product getProductById(UUID id) {
        return productRepository.getProductByIdAndIsActive(id, true).orElseThrow(() -> new IllegalArgumentException("Product does not exist"));
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


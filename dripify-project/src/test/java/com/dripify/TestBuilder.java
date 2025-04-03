package com.dripify;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.category.model.Category;
import com.dripify.product.model.Product;
import com.dripify.product.model.ProductImage;
import com.dripify.product.model.enums.*;
import com.dripify.review.model.Review;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.web.dto.CreateProductRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.mock;


public final class TestBuilder {
    private TestBuilder() {}

    public static User randomUser() {
        return User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .password("123123")
                .isActive(true)
                .favoriteProducts(Set.of())
                .shoppingCart(new ShoppingCart())
                .role(UserRole.USER)
                .build();
    }

    public static Product randomProduct() {
        User seller = User.builder().username("seller").reviews(List.of(new Review())).build();

        return Product.builder()
                .name("Test Product")
                .description("Test Product Description")
                .brand(Brand.LOUIS_VUITTON)
                .condition(Condition.NEW)
                .color(Color.BEIGE)
                .size(Size.M)
                .material(Material.COTTON)
                .category(new Category("Dresses", Gender.WOMEN))
                .gender(Gender.WOMEN)
                .seller(seller)
                .images(List.of(ProductImage.builder().imageUrl("image.com").build()))
                .build();
    }

    public static AuthenticationMetadata principal(User user) {
        return new AuthenticationMetadata(user.getId(), user.getUsername(), user.getPassword(), user.getRole(), user.isActive());
    }

    public static CreateProductRequest createProductRequest() {
        CreateProductRequest createProductRequest = new CreateProductRequest();
        createProductRequest.setImages(List.of(mock(MultipartFile.class), mock(MultipartFile.class)));
        createProductRequest.setTitle("Title Test Title Test");
        createProductRequest.setDescription("Description Description Description Description Description");
        createProductRequest.setBrand(Brand.LOUIS_VUITTON);
        createProductRequest.setSize(Size.M);
        createProductRequest.setCategory(new Category());
        createProductRequest.setGender(Gender.WOMEN);
        createProductRequest.setColor(Color.BLUE);
        createProductRequest.setPrice(BigDecimal.TEN);
        createProductRequest.setCondition(Condition.NEW);
        createProductRequest.setMaterial(Material.COTTON);
        return createProductRequest;
    }
}

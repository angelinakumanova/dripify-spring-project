package com.dripify;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.repository.ShoppingCartRepository;
import com.dripify.cart.service.ShoppingCartService;
import com.dripify.notification.service.NotificationService;
import com.dripify.product.model.Product;
import com.dripify.product.model.enums.Brand;
import com.dripify.product.model.enums.Color;
import com.dripify.product.model.enums.Condition;
import com.dripify.product.model.enums.Material;
import com.dripify.product.repository.ProductRepository;
import com.dripify.product.service.ProductService;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import com.dripify.user.repository.UserRepository;
import com.dripify.user.service.UserService;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
public class DeactivateUserITest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private NotificationService notificationService;

    private User user;

    @BeforeEach
    void setUp() {
        User rawUser = User.builder()
                .role(UserRole.USER)
                .firstName("test")
                .lastName("test")
                .username("testUser")
                .password("123123")
                .isActive(true)
                .email("test@test.com")
                .build();


        User cartProductSeller = User.builder()
                .role(UserRole.USER)
                .firstName("test")
                .lastName("test")
                .username("testUser1")
                .password("123123")
                .isActive(true)
                .email("test1@test.com")
                .build();

        User testedUser = userRepository.save(rawUser);
        User savedSeller = userRepository.save(cartProductSeller);


        Product product = Product.builder()
                .name("test")
                .description("test")
                .seller(testedUser)
                .brand(Brand.LOUIS_VUITTON)
                .color(Color.BLUE)
                .price(BigDecimal.TEN)
                .gender(Gender.WOMEN)
                .material(Material.LEATHER)
                .condition(Condition.ACCEPTABLE)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .isActive(true)
                .build();
        productRepository.save(product);

        testedUser.getProducts().add(product);

        Product cartProduct = Product.builder()
                .name("test")
                .description("test")
                .seller(savedSeller)
                .brand(Brand.LOUIS_VUITTON)
                .color(Color.BLUE)
                .price(BigDecimal.TEN)
                .gender(Gender.WOMEN)
                .material(Material.LEATHER)
                .condition(Condition.ACCEPTABLE)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .isActive(true)
                .build();

        Product savedProduct = productRepository.save(cartProduct);

        ShoppingCart shoppingCart = shoppingCartService.createNewCart(testedUser);
        shoppingCart.getProducts().add(savedProduct);
        shoppingCartRepository.save(shoppingCart);

        testedUser.setShoppingCart(shoppingCart);

        this.user = userRepository.save(testedUser);
    }

    @Test
    void testDeactivateUser_happyPath() {
        userService.deactivateUser(user);

        User updatedUser = userRepository.findById(user.getId()).orElseThrow();

        assertThat(updatedUser.isActive()).isFalse();
        assertThat(updatedUser.getUpdatedOn()).isNotNull();

        assertThat(shoppingCartRepository.findByUser(updatedUser).get().getProducts().size()).isEqualTo(0);
        assertFalse(notificationService.getNotificationPreference(user.getId()).isNewsletterEnabled());

        List<Product> userProducts = productRepository.findBySeller(user);
        assertThat(userProducts.stream().noneMatch(Product::isActive)).isTrue();
    }
}

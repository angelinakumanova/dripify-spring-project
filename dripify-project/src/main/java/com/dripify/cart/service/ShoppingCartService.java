package com.dripify.cart.service;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.repository.ShoppingCartRepository;
import com.dripify.exception.ShoppingCartException;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCart createNewCart(User user) {
        if (shoppingCartRepository.findByUser(user).isPresent()) {
            throw new ShoppingCartException("Shopping cart for user with id: [%s] already exists".formatted(user.getId()));
        }

        ShoppingCart newCart = ShoppingCart.builder()
                .user(user)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        return shoppingCartRepository.save(newCart);
    }

    public ShoppingCart getCart(User user) {
        return shoppingCartRepository.findByUser(user).orElseGet(() -> createNewCart(user));
    }

    public void addProduct(User user, Product product) {
        ShoppingCart userCart = getCart(user);

        List<Product> currentProducts = userCart.getProducts();

        if (currentProducts.contains(product)) {
            throw new ShoppingCartException("You have already added this product to the cart.");
        }

        currentProducts.add(product);
        shoppingCartRepository.save(userCart);
    }
}

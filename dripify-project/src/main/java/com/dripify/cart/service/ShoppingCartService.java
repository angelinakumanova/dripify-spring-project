package com.dripify.cart.service;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.repository.ShoppingCartRepository;
import com.dripify.exception.ShoppingCartException;
import com.dripify.product.model.Product;
import com.dripify.user.model.User;
import com.dripify.product.event.ProductDeactivationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
    }

    public ShoppingCart createNewCart(User user) {
        if (shoppingCartRepository.findByUser(user).isPresent()) {
            throw new IllegalArgumentException("Shopping cart for user with id: [%s] already exists".formatted(user.getId()));
        }

        return shoppingCartRepository.save(ShoppingCart.builder()
                .user(user)
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build());
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

    public boolean removeProduct(User user, Product product) {
        ShoppingCart userCart = getCart(user);

        List<Product> currentProducts = userCart.getProducts();

        boolean isRemoved = currentProducts.remove(product);
        shoppingCartRepository.save(userCart);

        return isRemoved;
    }

    public void clearCart(ShoppingCart shoppingCart) {
        shoppingCart.getProducts().clear();
        shoppingCartRepository.save(shoppingCart);
    }

    @EventListener
    @Transactional
    public void removeInactiveProduct(ProductDeactivationEvent event) {
        shoppingCartRepository.removeInactiveProduct(event.getProductId());
    }

    public ShoppingCart getCart(User user) {
        return shoppingCartRepository.findByUser(user).orElseGet(() -> {
            ShoppingCart build = ShoppingCart.builder()
                    .user(user)
                    .createdOn(LocalDateTime.now())
                    .updatedOn(LocalDateTime.now())
                    .build();

            return shoppingCartRepository.save(build);
        });
    }

    public void validateCartNotEmpty(User user) {
        Optional<ShoppingCart> cart = shoppingCartRepository.findByUser(user);

        if (cart.isEmpty() || cart.get().getProducts().isEmpty()) {
            throw new AccessDeniedException("Shopping cart is empty.");
        }
    }
}

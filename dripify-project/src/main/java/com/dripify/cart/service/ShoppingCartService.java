package com.dripify.cart.service;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.cart.repository.ShoppingCartRepository;
import com.dripify.exception.ShoppingCartException;
import com.dripify.user.model.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    public void addProduct(UUID productId) {

    }
}

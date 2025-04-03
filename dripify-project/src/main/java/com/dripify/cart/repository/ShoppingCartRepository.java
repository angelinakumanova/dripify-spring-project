package com.dripify.cart.repository;

import com.dripify.cart.model.ShoppingCart;
import com.dripify.user.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, UUID> {
    @EntityGraph(attributePaths = "products")
    Optional<ShoppingCart> findByUser(User user);

    @Modifying
    @Query(value = "DELETE FROM shopping_cart_products WHERE product_id = :productId", nativeQuery = true)
    void removeInactiveProduct(UUID productId);
}

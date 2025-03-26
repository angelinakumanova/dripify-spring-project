package com.dripify.user.repository;

import com.dripify.product.model.Product;
import com.dripify.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    Optional<User> findByUsername(String username);

    @Modifying
    @Query(value = "DELETE FROM user_favorite_products ufp WHERE ufp.product_id = :productId", nativeQuery = true)
    void removeFavouriteProduct(UUID productId);

    @Modifying
    @Query(value = "DELETE FROM shopping_cart_products scp WHERE scp.products_id = :productId", nativeQuery = true)
    void removeShoppingCartProduct(UUID productId);
}

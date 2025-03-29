package com.dripify.user.repository;

import com.dripify.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> getUserByUsername(String username);

    Optional<User> getUserByEmail(String email);

    Optional<User> findByUsernameAndIsActiveTrue(String username);

    @Modifying
    @Query(value = "DELETE FROM user_favorite_products ufp WHERE ufp.product_id = :productId", nativeQuery = true)
    void removeFavouriteProduct(UUID productId);


    Page<User> getAllByUsernameIsNot(String username, Pageable pageable);

    @Modifying
    @Query("UPDATE Product p SET p.isActive = false WHERE p.seller = :user")
    void deactivateProductsByUser(User user);
}

package com.dripify.product.repository;

import com.dripify.product.model.Product;
import com.dripify.shared.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
    SELECT p FROM Product p
    WHERE (:categoryName IS NULL OR p.category.name = :categoryName)
    AND (:gender IS NULL OR p.gender = :gender OR p.gender = com.dripify.shared.enums.Gender.UNISEX)
        """)
    List<Product> findProducts(
            @Param("categoryName") String categoryName,
            @Param("gender") Gender gender
    );
}

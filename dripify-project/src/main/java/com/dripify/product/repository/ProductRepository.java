package com.dripify.product.repository;

import com.dripify.product.model.Product;
import com.dripify.shared.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p" +
           " WHERE (:category IS NULL OR p.category.name = :category OR p.category.parentCategory.name = :category)" +
           " AND (:gender IS NULL OR p.gender = :gender)")
    Page<Product> findProductsByCategoryAndGender(
            @Param("category") String categoryName, Gender gender, Pageable pageable);

    Optional<Product> getProductById(UUID id);

    Page<Product> findAllByOrderByCreatedOnDesc(Pageable pageable);
}

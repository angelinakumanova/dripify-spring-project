package com.dripify.product.repository;

import com.dripify.product.model.Product;
import com.dripify.shared.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("SELECT p FROM Product p" +
           " WHERE (:category IS NULL OR p.category.name = :category OR p.category.parentCategory.name = :category)" +
           " AND (:genders IS NULL OR p.gender IN (:genders))")
    Page<Product> findProductsByCategoryAndGender(
            @Param("category") String categoryName, List<Gender> genders, Pageable pageable);

    Optional<Product> getProductById(UUID id);
}

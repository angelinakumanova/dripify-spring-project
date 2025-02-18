package com.dripify.product.repository;

import com.dripify.product.model.Product;
import com.dripify.web.dto.ProductFilter;
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
    Optional<Product> getProductById(UUID id);

    Page<Product> findAllByOrderByCreatedOnDesc(Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN c.parentCategory pc " +
            "WHERE (:#{#productFilter.subcategory} IS NOT NULL AND c.name = :#{#productFilter.subcategory}) " +
            "OR (:#{#productFilter.subcategory} IS NULL AND (c.name = :#{#productFilter.category} OR pc.name = :#{#productFilter.category})) " +
            "AND (p.gender = :#{#productFilter.gender}) " +
            "AND (:#{#productFilter.materials.size()} = 0 OR p.material IN :#{#productFilter.materials}) " +
            "AND (:#{#productFilter.brands.size()} = 0 OR p.brand IN :#{#productFilter.brands}) " +
            "AND (:#{#productFilter.colors.size()} = 0 OR p.color IN :#{#productFilter.colors}) " +
            "AND (:#{#productFilter.sizes.size()} = 0 OR p.size IN :#{#productFilter.sizes})")
    Page<Product> findProductsByFilters(@Param("productFilter") ProductFilter productFilter, Pageable pageable);
}

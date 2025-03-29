package com.dripify.product.repository;

import com.dripify.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.product.isActive = false")
    void deleteAllInactiveProductsImages();
}

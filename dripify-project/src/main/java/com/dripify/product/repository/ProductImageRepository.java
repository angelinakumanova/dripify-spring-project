package com.dripify.product.repository;

import com.dripify.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, UUID> {
}

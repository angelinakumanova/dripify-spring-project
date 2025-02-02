package com.dripify.product.repository;

import com.dripify.product.model.Product;
import com.dripify.shared.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findProductsByCategoryNameInAndGenderIn(List<String> categoryNames, List<Gender> gender);

    Optional<Product> getProductById(UUID id);
}

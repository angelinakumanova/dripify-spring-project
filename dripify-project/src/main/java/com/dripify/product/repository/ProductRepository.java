package com.dripify.product.repository;

import com.dripify.category.model.Category;
import com.dripify.product.model.Product;
import com.dripify.product.model.enums.Brand;
import com.dripify.product.model.enums.Color;
import com.dripify.product.model.enums.Material;
import com.dripify.product.model.enums.Size;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Query("""
    SELECT p FROM Product p 
    WHERE (p.category = :category OR p.category.parentCategory = :category) 
      AND p.gender = :gender
      AND (:brandsSize = 0 OR p.brand IN :brands)
      AND (:colorsSize = 0 OR p.color IN :colors)
      AND (:materialsSize = 0 OR p.material IN :materials)
      AND (:sizesSize = 0 OR p.size IN :sizes)
      AND p.isActive = true
      ORDER BY
            CASE WHEN :fieldSort = 'mostRated' THEN SIZE(p.seller.reviews) END DESC
              
    """)
    Page<Product> findByCategoryAndGenderAndFilters(Category category,
                                                    Gender gender,
                                                    List<Brand> brands, int brandsSize,
                                                    List<Color> colors, int colorsSize,
                                                    List<Material> materials, int materialsSize,
                                                    List<Size> sizes, int sizesSize,
                                                    String fieldSort,
                                                    Pageable pageable);

    Optional<Product> getProductByIdAndIsActiveTrue(UUID id);


    Page<Product> getProductsBySellerAndIsActiveTrue(User seller, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.category = :category AND p.gender = :gender AND " +
            "NOT p.id = :currentProductId ORDER BY p.createdOn DESC LIMIT :limit")
    List<Product> getProductsByCategoryOrderByCreatedOnDesc(Category category, Gender gender, UUID currentProductId, int limit);


    List<Product> getProductsBySellerAndIdNotAndIsActiveTrueOrderByCreatedOnDesc(User seller, UUID id);

    Page<Product> getAllByIsActiveTrueOrderByCreatedOnDesc(Pageable pageable);

    @Query("SELECT p.id FROM Product p WHERE p.isActive = false")
    List<UUID> getIdsByIsActiveFalse();

    void deleteAllByIsActiveFalse();

    @Modifying
    @Query("UPDATE Product p SET p.isActive = false WHERE p.seller = :seller")
    void deactivateUserProducts(User seller);

    List<Product> Size(Size size);
}

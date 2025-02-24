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
    """)
    Page<Product> findByCategoryAndGenderAndFilters(Category category,
                                                    Gender gender,
                                                    List<Brand> brands, int brandsSize,
                                                    List<Color> colors, int colorsSize,
                                                    List<Material> materials, int materialsSize,
                                                    List<Size> sizes, int sizesSize,
                                                    Pageable pageable);

    Optional<Product> getProductById(UUID id);


    Page<Product> getProductsBySeller(User user, Pageable pageable);
}

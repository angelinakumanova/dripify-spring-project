package com.dripify.category.repository;

import com.dripify.category.model.Category;
import com.dripify.shared.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentCategoryName(String name);

    Optional<Category> findByName(String categoryName);

    Optional<Category> findByNameAndParentCategoryName(String category, String parentCategoryName);


    @Query("SELECT c FROM Category c WHERE c.gender IN (:gender, 'UNISEX') AND c.parentCategory.name = :categoryName")
    List<Category> findByGenderAndMainCategory(Gender gender, String categoryName);

    List<Category> findAllByParentCategoryIsNull();
}

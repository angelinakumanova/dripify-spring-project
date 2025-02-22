package com.dripify.category.repository;

import com.dripify.category.model.Category;
import com.dripify.shared.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByParentCategoryIsNull();

    Optional<Category> findByName(String category);

    Optional<Category> findByNameAndParentCategoryName(String category, String parentCategoryName);
}

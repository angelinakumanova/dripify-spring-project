package com.dripify.category.service;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.CategoryResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Cacheable("categories")
    public List<Category> getMainCategories() {
        return categoryRepository.findAllByParentCategoryIsNull();
    }

    public Category getByName(String category) {
        return categoryRepository.findByName(category).orElseThrow(() -> new IllegalArgumentException("Category [%s] not found".formatted(category)));
    }

    public Category getByNameAndParentCategory(String categoryName, String parentCategoryName) {
        return categoryRepository.findByNameAndParentCategoryName(categoryName, parentCategoryName)
                .orElseThrow(() -> new IllegalArgumentException("Category [%s] in [%s] not found".formatted(categoryName, parentCategoryName)));
    }

    public List<Category> getSubcategories() {
        return categoryRepository.findAllBySubcategoriesIsNull();
    }



    public List<CategoryResponse> getCategoriesByGenderAndMainCategory(Gender gender, String categoryName) {
        return categoryRepository.findByGenderAndMainCategory(gender, categoryName)
                .stream()
                .map(category -> new CategoryResponse(category.getId(), category.getName()))
                .toList();
    }
}

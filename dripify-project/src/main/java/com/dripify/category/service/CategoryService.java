package com.dripify.category.service;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.exception.DomainException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> getMainCategories() {
        return categoryRepository.findAllByParentCategoryIsNull();
    }

    public Category getByName(String category) {
        return categoryRepository.findByName(category).orElseThrow(() -> new DomainException("Category [%s] not found".formatted(category)));
    }

    public Category getByNameAndParentCategory(String categoryName, String parentCategoryName) {
        return categoryRepository.findByNameAndParentCategoryName(categoryName, parentCategoryName)
                .orElseThrow(() -> new DomainException("Category [%s] in [%s] not found".formatted(categoryName, parentCategoryName)));
    }
}

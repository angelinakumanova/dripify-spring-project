package com.dripify.category.service;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.shared.enums.Gender;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable("categoriesByGender")
    public List<Category> getCategoriesByGenders(List<Gender> genders) {
        return categoryRepository.findByGenderInAndParentCategoryIsNull(genders);
    }

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}

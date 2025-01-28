package com.dripify.category.service;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.shared.enums.Gender;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    public final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Cacheable("categoriesByGender")
    public List<Category> getCategoriesByGenders(List<Gender> genders) {
        return categoryRepository.findByGenderIn(genders);
    }
}

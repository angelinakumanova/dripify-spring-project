package com.dripify.shared.advice;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.shared.enums.Gender;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Map;

@ControllerAdvice
public class NavBarInitializer {

    private final CategoryService categoryService;

    public NavBarInitializer(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categoriesForWomen")
    public List<Category> getCategoriesForWomen() {
        return categoryService.getCategoriesByGenders(List.of(Gender.WOMEN, Gender.UNISEX));
    }

    @ModelAttribute("categoriesForMen")
    public List<Category> getCategoriesForMen() {
        return categoryService.getCategoriesByGenders(List.of(Gender.MEN, Gender.UNISEX));
    }
}

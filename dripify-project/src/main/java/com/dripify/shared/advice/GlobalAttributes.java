package com.dripify.shared.advice;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalAttributes {

    private final CategoryService categoryService;

    public GlobalAttributes(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ModelAttribute("categories")
    public List<Category> getMainCategories() {

        return categoryService.getMainCategories();
    }

}

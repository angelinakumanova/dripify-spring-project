package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.CategoryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryRestController {

    private final CategoryService categoryService;

    public CategoryRestController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getSubcategories(@RequestParam Gender gender, @RequestParam String categoryName) {
        return ResponseEntity.ok(categoryService.getCategoriesByGenderAndMainCategory(gender, categoryName));
    }
}

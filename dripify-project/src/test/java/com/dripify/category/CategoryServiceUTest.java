package com.dripify.category;

import com.dripify.category.model.Category;
import com.dripify.category.repository.CategoryRepository;
import com.dripify.category.service.CategoryService;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceUTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void whenGetAllSubcategories_thenReturnExpectedList() {
        // Given
        Category clothingSub1 = new Category("T-Shirts", Gender.UNISEX);
        Category clothingSub2 = new Category("Jackets", Gender.UNISEX);
        Category shoes = new Category("Shoes", Gender.UNISEX);
        Category accessories = new Category("Accessories", Gender.UNISEX);

        List<Category> clothingSubcategories = new ArrayList<>(List.of(clothingSub1, clothingSub2));

        when(categoryRepository.findAllByParentCategoryName("Clothing")).thenReturn(clothingSubcategories);
        when(categoryRepository.findByName("Shoes")).thenReturn(Optional.of(shoes));
        when(categoryRepository.findByName("Accessories")).thenReturn(Optional.of(accessories));

        // When
        List<Category> result = categoryService.getAllSubcategories();

        // Then
        assertEquals(4, result.size());
        assertTrue(result.contains(clothingSub1));
        assertTrue(result.contains(clothingSub2));
        assertTrue(result.contains(shoes));
        assertTrue(result.contains(accessories));

        verify(categoryRepository).findAllByParentCategoryName("Clothing");
        verify(categoryRepository).findByName("Shoes");
        verify(categoryRepository).findByName("Accessories");
    }

    @Test
    void givenHappyPath_whenGetAllMainCategories_thenReturnExpectedList() {
        // Given
        Category clothing = new Category("Clothing", Gender.UNISEX);
        Category shoes = new Category("Shoes", Gender.UNISEX);
        Category accessories = new Category("Accessories", Gender.UNISEX);

        List<Category> mainCategories = new ArrayList<>(List.of(clothing, shoes, accessories));

        when(categoryRepository.findAllByParentCategoryIsNull()).thenReturn(mainCategories);

        // When
        List<Category> result = categoryService.getMainCategories();

        // Then
        assertEquals(mainCategories.size(), result.size());
    }

    @Test
    void givenValidCategoryName_whenGetByName_thenReturnExpectedCategory() {
        // Given
        Category clothing = new Category("Clothing", Gender.UNISEX);
        when(categoryRepository.findByName("Clothing")).thenReturn(Optional.of(clothing));

        // When
        Category result = categoryService.getByName("Clothing");

        // Then
        assertEquals(clothing, result);
    }

    @Test
    void givenInvalidCategoryName_whenGetByName_thenThrowsException() {
        // Given
        when(categoryRepository.findByName("Gosho")).thenReturn(Optional.empty());

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> categoryService.getByName("Gosho"));
    }

    @Test
    void givenValidCategoryNameAndParentCategoryName_whenGetByNameAndParentCategory_thenReturnExpectedCategory() {
        // Given
        Category clothing = new Category("Clothing", Gender.UNISEX);
        Category dresses = new Category("Dresses", Gender.WOMEN);
        dresses.setParentCategory(clothing);

        when(categoryRepository.findByNameAndParentCategoryName("Dresses", "Clothing")).thenReturn(Optional.of(dresses));

        // When
        Category result = categoryService.getByNameAndParentCategory("Dresses", "Clothing");

        // Then
        assertEquals(dresses, result);
    }

    @Test
    void givenInvalidCategoryNameAndValidParentCategoryName_whenGetByNameAndParentCategory_thenThrowsException() {
        // Given
        when(categoryRepository.findByNameAndParentCategoryName("Gosho", "Clothing")).thenReturn(Optional.empty());

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> categoryService.getByNameAndParentCategory("Gosho", "Clothing"));
    }

    @Test
    void givenValidGenderAndValidMainCategory_whenGetCategoriesByGenderAndMainCategory_thenReturnsCorrectCategories() {
        // Given
        Gender gender = Gender.MEN;
        Category clothingParentCategory = new Category();
        clothingParentCategory.setName("Clothing");

        Category tShirtsCategory = new Category("T-Shirts", gender);
        tShirtsCategory.setId(1L);
        tShirtsCategory.setParentCategory(clothingParentCategory);

        Category jeansCategory = new Category("Jeans", gender);
        jeansCategory.setId(2L);
        jeansCategory.setParentCategory(clothingParentCategory);

        List<Category> mockCategories = List.of(
                tShirtsCategory,
               jeansCategory
        );

        when(categoryRepository.findByGenderAndMainCategory(gender, clothingParentCategory.getName())).thenReturn(mockCategories);

        // When
        List<CategoryResponse> result = categoryService.getCategoriesByGenderAndMainCategory(gender, clothingParentCategory.getName());

        // Then
        assertEquals(2, result.size());
        assertEquals("T-Shirts", result.get(0).getName());
        assertEquals("Jeans", result.get(1).getName());

        verify(categoryRepository, times(1)).findByGenderAndMainCategory(gender, clothingParentCategory.getName());


    }
}

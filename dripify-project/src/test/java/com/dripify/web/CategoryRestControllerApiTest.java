package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.shared.enums.Gender;
import com.dripify.web.dto.CategoryResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryRestController.class)
public class CategoryRestControllerApiTest {

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getSubcategories_happyPath() throws Exception {
        List<CategoryResponse> mockCategories = List.of(
                new CategoryResponse(1, "Dresses"),
                new CategoryResponse(2, "Skirts")
        );

        when(categoryService.getCategoriesByGenderAndMainCategory(Gender.WOMEN, "Clothing"))
                .thenReturn(mockCategories);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/categories")
                        .param("gender", "WOMEN")
                        .param("categoryName", "Clothing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(mockCategories.size()))
                .andExpect(jsonPath("$[0].name").value("Dresses"))
                .andExpect(jsonPath("$[1].name").value("Skirts"));

        verify(categoryService, times(1)).getCategoriesByGenderAndMainCategory(Gender.WOMEN, "Clothing");
    }
}

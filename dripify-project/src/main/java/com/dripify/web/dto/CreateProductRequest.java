package com.dripify.web.dto;

import com.dripify.category.model.Category;
import com.dripify.product.model.enums.*;
import com.dripify.shared.enums.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    private String title;
    private String description;
    private Gender gender;
    @NotNull
    private Category category;
    @NotNull
    private Brand brand;
    private Material material;
    private Color color;
    private Size size;
    private Condition condition;
    private BigDecimal price;
}

package com.dripify.web.dto;

import com.dripify.product.model.enums.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ProductEditRequest {
    @NotNull(message = "Title cannot be null!")
    @jakarta.validation.constraints.Size(min = 10, max = 30, message = "Title must be 10-30 characters. Keep it clear and catchy!")
    private String title;

    @NotNull(message = "Description cannot be null!")
    @jakarta.validation.constraints.Size(min = 50, max = 200, message = "Your description should be between 50 to 200 characters to make it impactful and to the point.")
    private String description;

    @NotNull(message = "Brand is required!")
    private Brand brand;

    @NotNull(message = "Material is required!")
    private Material material;

    @NotNull(message = "Color is required!")
    private Color color;

    @NotNull(message = "Size is required!")
    private Size size;

    @NotNull(message = "Condition is required!")
    private Condition condition;

    @NotNull(message = "Missing price. Please enter a valid amount.")
    @Min(value = 1, message = "Missing price. Please enter a valid amount.")
    private BigDecimal price;
}

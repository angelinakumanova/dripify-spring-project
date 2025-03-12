package com.dripify.web.dto;

import com.dripify.category.model.Category;
import com.dripify.product.model.enums.*;
import com.dripify.shared.enums.Gender;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CreateProductRequest {
    @NotNull(message = "Show us more! At least two photos are needed to upload your product.")
    @jakarta.validation.constraints.Size(min = 2, message = "Show us more! At least two photos are needed to upload your product.")
    @jakarta.validation.constraints.Size(max = 5, message = "Max photos uploaded. Need to swap one? Delete an image to add a new one.")
    private List<MultipartFile> images;

    @NotNull(message = "Title cannot be null!")
    @jakarta.validation.constraints.Size(min = 10, max = 30, message = "Title must be 10-30 characters. Keep it clear and catchy!")
    private String title;

    @NotNull(message = "Description cannot be null!")
    @jakarta.validation.constraints.Size(min = 50, max = 200, message = "Your description should be between 50 to 200 characters to make it impactful and to the point.")
    private String description;

    @NotNull(message = "Gender is required!")
    private Gender gender;

    @NotNull(message = "Category is required!")
    private Category category;

    @NotNull(message = "Brand is required!")
    private Brand brand;

    @NotNull(message = "Material is required!")
    private Material material;

    @NotNull(message = "Color is required!")
    private Color color;

    private Size size;

    @NotNull(message = "Condition is required!")
    private Condition condition;

    @NotNull(message = "Missing price. Please enter a valid amount.")
    @Min(value = 1, message = "Missing price. Please enter a valid amount.")
    private BigDecimal price;


    @AssertTrue(message = "Size is required!")
    public boolean size() {

        if (category == null) {
            return true;
        }

        if ("accessories".equalsIgnoreCase(category.getParentCategory().getName()) && size == null) {
            return true;
        }

        if ("clothing".equalsIgnoreCase(category.getParentCategory().getName()) && SizeCategory.CLOTHING.equals(size.getCategory())) {
            return true;
        }

        if ("shoes".equalsIgnoreCase(category.getParentCategory().getName()) && SizeCategory.SHOE.equals(size.getCategory())) {
            return true;
        }

        return false;
    }
}

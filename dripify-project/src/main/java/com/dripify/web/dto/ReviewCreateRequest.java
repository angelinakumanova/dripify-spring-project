package com.dripify.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewCreateRequest {

    @NotNull(message = "Title must NOT be null!")
    @Size(min = 5, max = 20, message = "Title must be between 5 and 20 characters!")
    private String title;

    @NotNull(message = "Comment must NOT be null!")
    @Size(min = 10, max = 100, message = "Comment must be between 10 and 100 characters!")
    private String comment;

    @Min(value = 1, message = "The rating must be at least 1.")
    @Max(value = 5, message = "The rating cannot exceed 5.")
    private int rating;
}

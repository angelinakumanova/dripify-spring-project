package com.dripify.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserEditRequest {
    private String imageUrl;

    @NotNull(message = "First name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid first name. Only alphabetic characters are allowed.")
    private String firstName;

    @NotNull(message = "Last name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid last name. Only alphabetic characters are allowed.")
    private String lastName;

    @Size(max = 160)
    private String description;
}

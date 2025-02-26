package com.dripify.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UserEditRequest {

    private MultipartFile profilePicture;

    private Boolean deletePicture;

    @NotNull(message = "First name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid first name. Only alphabetic characters are allowed.")
    private String firstName;

    @NotNull(message = "Last name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid last name. Only alphabetic characters are allowed.")
    private String lastName;

    @Size(max = 160, message = "Description must be a maximum of 160 characters.")
    private String description;
}

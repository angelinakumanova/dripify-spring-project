package com.dripify.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotNull(message = "Username must NOT be null!")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{4,20}$", message = "Use only letters & numbers, 5–20 characters.")
    private String username;

    @NotNull(message = "First name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid first name. Only alphabetic characters are allowed.")
    private String firstName;

    @NotNull(message = "Last name must NOT be null!")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Please enter a valid last name. Only alphabetic characters are allowed.")
    private String lastName;

    @NotNull(message = "Email must NOT be null!")
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Please enter a valid email.")
    private String email;

    @NotNull(message = "Password must NOT be null!")
    @Pattern(regexp = "^[A-Za-z\\d]{6,20}$", message = "Password must be 6-20 characters long and contain only letters and digits.")
    private String password;

    private String confirmPassword;

    @AssertTrue(message = "Passwords do not match!")
    public boolean isPasswordMatch() {
        if (password == null || confirmPassword == null) {
            return false;
        }

        return password.equals(confirmPassword);
    }
}

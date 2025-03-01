package com.dripify.web.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordUpdateRequest {

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

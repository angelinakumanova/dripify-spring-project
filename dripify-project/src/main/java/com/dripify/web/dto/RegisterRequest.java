package com.dripify.web.dto;

import com.dripify.validation.MatchFields;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@MatchFields(firstField = "password", secondField = "confirmPassword")
public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "Use only letters & numbers, 5–20 characters.")
    private String username;
    @Size(min = 1)
    private String firstName;
    @Size(min = 1)
    private String lastName;
    @Email(regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
    private String password;
    private String confirmPassword;
}

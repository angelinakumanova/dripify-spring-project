package com.dripify.web.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
@MatchFields(firstField = "password", secondField = "confirmPassword")
public class RegisterRequest {
    @Size(min = 5, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9]$")
    private String username;
    @Size(min = 2)
    private String firstName;
    @Size(min = 2)
    private String lastName;
    @NotBlank
    @Email
    private String email;
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
    private String password;
    private String confirmPassword;

}

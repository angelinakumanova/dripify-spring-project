package com.dripify.web.dto;

import jakarta.validation.constraints.*;

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
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")
    private String password;

    private String confirmPassword;

    public @Size(min = 5, max = 30, message = "Length must be between 5 and 30 chars") @Pattern(regexp = "^[a-zA-Z0-9]$") String getUsername() {
        return username;
    }

    public void setUsername(@Size(min = 5, max = 30, message = "Length must be between 5 and 30 chars") @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{4,}$") String username) {
        this.username = username;
    }

    public @Size(min = 2) String getFirstName() {
        return firstName;
    }

    public void setFirstName(@Size(min = 2) String firstName) {
        this.firstName = firstName;
    }

    public @Size(min = 2) String getLastName() {
        return lastName;
    }

    public void setLastName(@Size(min = 2) String lastName) {
        this.lastName = lastName;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$") String getPassword() {
        return password;
    }

    public void setPassword(@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$") String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}

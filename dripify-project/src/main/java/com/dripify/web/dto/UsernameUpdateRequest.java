package com.dripify.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsernameUpdateRequest {

    @NotNull(message = "Username cannot be null!")
    @Pattern(regexp = "^[a-zA-Z][a-zA-Z0-9]{4,20}$", message = "Use only letters & numbers, 5–20 characters.")
    private String username;
}

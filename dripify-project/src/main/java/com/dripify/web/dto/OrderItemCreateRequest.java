package com.dripify.web.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemCreateRequest {

    @NotNull(message = "User Id must NOT be empty!")
    private UUID userId;

    private String name;

    private String size;

    private String gender;

    private String category;

    private BigDecimal price;
}

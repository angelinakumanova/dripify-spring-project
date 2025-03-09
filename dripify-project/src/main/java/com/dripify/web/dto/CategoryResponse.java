package com.dripify.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponse {
    private long id;
    private String name;
}

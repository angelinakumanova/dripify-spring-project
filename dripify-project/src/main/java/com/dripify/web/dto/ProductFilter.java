package com.dripify.web.dto;

import com.dripify.product.model.enums.Brand;
import com.dripify.product.model.enums.Color;
import com.dripify.product.model.enums.Material;
import com.dripify.product.model.enums.Size;
import com.dripify.shared.enums.Gender;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductFilter {
    private Gender gender;
    private String category;
    private String subcategory;
    private List<Size> sizes;
    private List<Material> materials;
    private List<Brand> brands;
    private List<Color> colors;

    public ProductFilter() {
        this.sizes = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.brands = new ArrayList<>();
        this.colors = new ArrayList<>();
    }

    public void setGender(String gender) {
        this.gender = Gender.valueOf(gender.toUpperCase());
    }
}

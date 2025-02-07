package com.dripify.web.controller;

import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.shared.enums.Gender;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/{gender}/{category}", "/{gender}/all", "", "/{category}"})
    public ModelAndView getFilteredProducts(@PathVariable(required = false) String gender,
                                            @PathVariable(required = false) String category,
                                            @RequestParam(defaultValue = "0") int page,
                                            @RequestParam(defaultValue = "10") int size,
                                            HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        List<Gender> genderFilter = (gender != null) ? List.of(Gender.valueOf(gender.toUpperCase())) : null;

        Page<Product> productPage = productService.getFilteredProducts(category, genderFilter, page, size);

        String currentCategory;
        if (gender != null && category != null) {
            currentCategory = String.format("%s's %s", gender, category);
        } else if (gender != null) {
            currentCategory = gender + "'s Products";
        } else if (category != null) {
            currentCategory = category;
        } else {
            currentCategory = "All Products";
        }

        modelAndView.addObject("productPage", productPage);
        modelAndView.addObject("currentPage", productPage.getNumber());
        modelAndView.addObject("currentCategory", currentCategory);
        modelAndView.addObject("currentPath", request.getContextPath());

        return modelAndView;
    }


    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }


}

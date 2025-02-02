package com.dripify.web.controller;

import com.dripify.product.service.ProductService;
import com.dripify.shared.enums.Gender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProducts(@PathVariable(name = "category") String categoryName,
                                    @PathVariable String gender) {
        ModelAndView modelAndView = new ModelAndView("products/products");

        modelAndView.addObject("products", productService
                .getFilteredProducts(categoryName, Gender.valueOf(gender.toUpperCase())));

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }


}

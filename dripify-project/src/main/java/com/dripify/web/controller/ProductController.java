package com.dripify.web.controller;

import com.dripify.product.service.ProductService;
import com.dripify.shared.enums.Gender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProducts(@PathVariable String category,
                                    @PathVariable String gender) {
        ModelAndView modelAndView = new ModelAndView("products/products");

        String actualCategory = category.replace("-", " & ");
        modelAndView.addObject("products", productService
                .getFilteredProducts(actualCategory, Gender.valueOf(gender.toUpperCase())));

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }


}

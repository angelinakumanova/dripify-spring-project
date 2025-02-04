package com.dripify.web.controller;

import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.shared.enums.Gender;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
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

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProducts(@PathVariable String category,
                                    @PathVariable String gender,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "20") int size) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        Page<Product> productPage = productService.getFilteredProducts(category, List.of(Gender.valueOf(gender.toUpperCase())), page, size);

        modelAndView.addObject("products", productPage.getContent());
        modelAndView.addObject("currentPage", productPage.getNumber());
        modelAndView.addObject("totalPages", productPage.getTotalPages());
        modelAndView.addObject("totalItems", productPage.getTotalElements());

        return modelAndView;
    }


    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }


}

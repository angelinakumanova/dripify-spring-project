package com.dripify.web.controller;

import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
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

    @GetMapping("")
    public ModelAndView getAllProducts(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest request) {

        Page<Product> productPage = productService.getFilteredProducts(null, null, page, size);

        ModelAndView modelAndView = new ModelAndView("/products/products");
        addModelAttributes(request, "All Products", modelAndView, productPage);

        return modelAndView;
    }

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProductsByGenderAndCategory(@PathVariable String gender,
                                                       @PathVariable String category,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       HttpServletRequest request) {

        Page<Product> productPage = productService.getFilteredProducts(category, gender, page, size);

        ModelAndView modelAndView = new ModelAndView("/products/products");
        addModelAttributes(request,  (gender + "'s " + category), modelAndView, productPage);

        return modelAndView;
    }

    @GetMapping("/category/{category}")
    public ModelAndView getProductsByCategory(@PathVariable String category,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "10") int size,
                                              HttpServletRequest request) {
        Page<Product> productPage = productService.getFilteredProducts(category, null, page, size);

        ModelAndView modelAndView = new ModelAndView("/products/products");
        addModelAttributes(request, category, modelAndView, productPage);

        return modelAndView;
    }

    @GetMapping("/{gender}/all")
    public ModelAndView getAllProductsByGender(@PathVariable String gender,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest request) {
        Page<Product> productPage = productService.getFilteredProducts(null, gender, page, size);

        ModelAndView modelAndView = new ModelAndView("/products/products");
        addModelAttributes(request, (gender + "'s products") ,modelAndView, productPage);

        return modelAndView;
    }



    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }



    private static void addModelAttributes(HttpServletRequest request,
                                           String currentCategory,
                                           ModelAndView modelAndView,
                                           Page<Product> productPage) {
        modelAndView.addObject("productPage", productPage);
        modelAndView.addObject("currentPage", productPage.getNumber());
        modelAndView.addObject("currentCategory", currentCategory);
        modelAndView.addObject("currentPath", request.getContextPath());
    }
}

package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.web.dto.ProductFilter;
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
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProductsByGenderAndCategory(@PathVariable String gender, @PathVariable String category,
            ProductFilter productFilter, @RequestParam(defaultValue = "0") int page, HttpServletRequest request) {

        Page<Product> productsPage = productService.getProductsByGenderAndCategory(gender, category, productFilter, page);

        return createProductsView(productsPage, category, request);
    }

    @GetMapping("/{gender}/{category}/{subcategory}")
    public ModelAndView getProductsByGenderAndSubcategory(@PathVariable String gender, @PathVariable String category,
             @PathVariable String subcategory, ProductFilter productFilter, @RequestParam(defaultValue = "0") int page,
                                                          HttpServletRequest request) {

        Page<Product> productsPage = productService.getProductsByGenderAndSubcategory(gender, category, subcategory,
                                                                                    productFilter, page);

        return createProductsView(productsPage, subcategory, request);
    }


    @GetMapping("/{id}")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }

    @GetMapping("/sell-product")
    public ModelAndView sellProduct() {
        ModelAndView modelAndView = new ModelAndView("/products/sell-product");

        return modelAndView;
    }

    private ModelAndView createProductsView(Page<Product> productsPage, String category, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        modelAndView.addObject("categories", categoryService.getMainCategories());
        modelAndView.addObject("productPage", productsPage);
        modelAndView.addObject("currentCategory", category);
        modelAndView.addObject("currentPath", request.getRequestURI());

        return modelAndView;
    }

}

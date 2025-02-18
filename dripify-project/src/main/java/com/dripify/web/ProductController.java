package com.dripify.web;

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

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/{gender}/{category}", "/{gender}/{category}/{subcategory}"})
    public ModelAndView getProductsByGenderAndCategory(ProductFilter productFilter,
                                                       @RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size,
                                                       HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView("/products/products");

        Page<Product> productPage = productService.getFilteredProducts(productFilter, page, size);
        addModelAttributes(request, productFilter.getCategory(), modelAndView, productPage);

        return modelAndView;
    }

    @GetMapping("/new-arrivals")
    public ModelAndView getNewArrivals(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       HttpServletRequest request) {
        Page<Product> productPage = productService.getNewArrivalsProducts(page, size);

        ModelAndView modelAndView = new ModelAndView("/products/new-arrivals");
        addModelAttributes(request, "New Arrivals", modelAndView, productPage);

        return modelAndView;
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


    private void addModelAttributes(HttpServletRequest request,
                                           String currentCategory,
                                           ModelAndView modelAndView,
                                           Page<Product> productPage) {
        modelAndView.addObject("productPage", productPage);
        modelAndView.addObject("currentPage", productPage.getNumber());
        modelAndView.addObject("currentCategory", currentCategory);
        modelAndView.addObject("currentPath", request.getRequestURI());

    }
}

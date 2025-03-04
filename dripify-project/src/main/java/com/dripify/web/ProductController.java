package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import com.dripify.web.dto.ProductFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final UserService userService;

    public ProductController(ProductService productService, CategoryService categoryService, UserService userService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/{gender}/{category}")
    public ModelAndView getProductsByGenderAndCategory(@PathVariable String gender, @PathVariable String category,
                                                       @ModelAttribute ProductFilter productFilter,
                                                       @RequestParam(defaultValue = "0") int page, HttpServletRequest request) {

        Page<Product> productsPage = productService.getFilteredProducts(gender, category, null, productFilter, page);

        return createProductsView(productsPage, category, request);
    }

    @GetMapping("/{gender}/{category}/{subcategory}")
    public ModelAndView getProductsByGenderAndSubcategory(@PathVariable String gender, @PathVariable String category,
                                                          @PathVariable String subcategory, @ModelAttribute ProductFilter productFilter,
                                                          @RequestParam(defaultValue = "0") int page, HttpServletRequest request) {

        Page<Product> productsPage = productService.getFilteredProducts(gender, category, subcategory,
                                                                                    productFilter, page);

        return createProductsView(productsPage, subcategory, request);
    }


    @GetMapping("/{id}/product")
    public ModelAndView getProduct(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("/products/single-product");
        modelAndView.addObject("product", productService.getProductById(id));

        return modelAndView;
    }

    @GetMapping("/new")
    public ModelAndView sellProduct() {
        ModelAndView modelAndView = new ModelAndView("/products/sell-product");
        modelAndView.addObject("createProductRequest", new CreateProductRequest());
        modelAndView.addObject("subcategories", categoryService.getSubcategories());

        return modelAndView;
    }

    @PostMapping("/new")
    public ModelAndView addNewProduct(@Valid CreateProductRequest createProductRequest, BindingResult bindingResult,
                                      @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("/products/sell-product");
            modelAndView.addObject("subcategories", categoryService.getSubcategories());

            return modelAndView;
        }

        productService.addNewProduct(createProductRequest, userService.getById(authenticationMetadata.getUserId()));

        return new ModelAndView("redirect:/products/new");
    }


    private ModelAndView createProductsView(Page<Product> productsPage, String category,
                                            HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        modelAndView.addObject("productPage", productsPage);
        modelAndView.addObject("currentCategory", category);
        modelAndView.addObject("currentPath", request.getRequestURI());

        // ToDO: Implement query string in another way
        String filters = request.getQueryString() == null ? "" : request.getQueryString();
        filters = "?" + filters;
        filters = filters.replaceAll("&?page=\\d+", "");

        modelAndView.addObject("filters", filters);


        return modelAndView;
    }

}

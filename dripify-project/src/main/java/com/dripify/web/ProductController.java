package com.dripify.web;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.ProductFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
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
                                                       @ModelAttribute ProductFilter productFilter, @RequestParam(defaultValue = "0") int page, HttpServletRequest request,
                                                       @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        Page<Product> productsPage = productService.getFilteredProducts(gender, category, null, productFilter, page);

        return createProductsView(productsPage, category, request, authenticationMetadata);
    }

    @GetMapping("/{gender}/{category}/{subcategory}")
    public ModelAndView getProductsByGenderAndSubcategory(@PathVariable String gender, @PathVariable String category,
                                                          @PathVariable String subcategory, @ModelAttribute ProductFilter productFilter,
                                                          @RequestParam(defaultValue = "0") int page, HttpServletRequest request, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        Page<Product> productsPage = productService.getFilteredProducts(gender, category, subcategory,
                                                                                    productFilter, page);

        return createProductsView(productsPage, subcategory, request, authenticationMetadata);
    }


    @GetMapping("/{id}/product")
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

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getMainCategories();
    }

    private ModelAndView createProductsView(Page<Product> productsPage, String category,
                                            HttpServletRequest request, AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        if (authenticationMetadata != null) {
            User user = userService.getById(authenticationMetadata.getUserId());
            modelAndView.addObject("user", user);
        }

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

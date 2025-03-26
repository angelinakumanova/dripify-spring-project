package com.dripify.web;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.ProductFilter;
import com.dripify.web.mapper.DtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @ModelAttribute("mainCategories")
    public List<Category> mainCategories() {
        return categoryService.getMainCategories();
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

    @GetMapping("/new-arrivals")
    public ModelAndView getNewArrivalsPage(@RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/products/new-arrivals");
        Page<Product> products = productService.getNewProducts(page);
        modelAndView.addObject("productPage", products);

        return modelAndView;
    }


    @GetMapping("/{id}")
    public String getProduct(@PathVariable UUID id, Model model) {

        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        if (!model.containsAttribute("productEditRequest")) {
            model.addAttribute("productEditRequest", DtoMapper.mapToProductEditRequest(product));
        }

        if (model.containsAttribute("org.springframework.validation.BindingResult.productEditRequest")) {
            BindingResult bindingResult = (BindingResult) model.getAttribute("org.springframework.validation.BindingResult.productEditRequest");
            model.addAttribute("bindingResult", bindingResult);
        }


        int productsFetchLimit = 4;
        model.addAttribute("similarProducts", productService.getSimilarProducts(productsFetchLimit, product));
        model.addAttribute("sellerMoreProducts", productService.getUserLatestProducts(product.getSeller(), product.getId()));

        return "/products/single-product";
    }

    @PutMapping("/{id}/edit")
    public String editProduct(@PathVariable UUID id, @Valid ProductEditRequest productEditRequest, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {


        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productEditRequest", productEditRequest);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productEditRequest", bindingResult);


            return "redirect:/products/" + id;
        }

        Product product = productService.getProductById(id);
        productService.editProduct(product, productEditRequest, userService.getById(authenticationMetadata.getUserId()));

        return "redirect:/products/" + id;
    }

    @PutMapping("/{id}/status")
    public String updateProductStatus(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        Product product = productService.getProductById(id);

        productService.deactivateProduct(product, userService.getById(authenticationMetadata.getUserId()));

        return "redirect:/users/" + authenticationMetadata.getUsername() + "/profile/products";
    }

    @GetMapping("/new")
    public ModelAndView getAddNewProductPage() {

        return new ModelAndView("/products/sell-product");
    }

    @PostMapping("/{id}/favourite")
    public String favouriteProduct(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        Product product = productService.getProductById(id);
        User user = userService.getById(authenticationMetadata.getUserId());

        userService.favoriteProduct(product, user);

        return "redirect:/products/" + id;
    }

    @DeleteMapping("/{id}/favourite")
    public String removeFavourite(@PathVariable UUID id,
                                  @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                  @RequestHeader(value = "Referer", required = false) String referer) {
        Product product = productService.getProductById(id);
        User user = userService.getById(authenticationMetadata.getUserId());

        userService.removeFavoriteProduct(product, user);

        return "redirect:" + referer;
    }


    private ModelAndView createProductsView(Page<Product> productsPage, String category,
                                            HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("/products/products");

        modelAndView.addObject("productPage", productsPage);
        modelAndView.addObject("currentCategory", category);
        modelAndView.addObject("currentPath", request.getRequestURI());

        String filters = request.getQueryString() == null ? "" : request.getQueryString();
        filters = "?" + filters;
        filters = filters.replaceAll("&?page=\\d+", "");

        modelAndView.addObject("filters", filters);


        return modelAndView;
    }

}

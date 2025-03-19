package com.dripify.web;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class ShoppingController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ProductService productService;

    public ShoppingController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/{productId}/add")
    public String addProductToCart(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                   @PathVariable UUID productId) {
        User user = userService.getById(authenticationMetadata.getUserId());
        Product product = productService.getProductById(productId);

        shoppingCartService.addProduct(user, product);

        return "redirect:/products/" + productId;
    }
}

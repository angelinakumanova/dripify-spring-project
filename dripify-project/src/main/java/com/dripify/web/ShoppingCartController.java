package com.dripify.web;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;
    private final UserService userService;
    private final ProductService productService;

    public ShoppingCartController(ShoppingCartService shoppingCartService, UserService userService, ProductService productService) {
        this.shoppingCartService = shoppingCartService;
        this.userService = userService;
        this.productService = productService;
    }

    @PostMapping("/{productId}")
    public String addProductToCart(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                   @PathVariable UUID productId,
                                   @RequestHeader(value = "Referer", required = false) String referer) {
        User user = userService.getById(authenticationMetadata.getUserId());
        Product product = productService.getProductById(productId);

        shoppingCartService.addProduct(user, product);

        return "redirect:" + referer;
    }

    @DeleteMapping("/{productId}")
    public String removeProductFromCart(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                                @PathVariable UUID productId,
                                        @RequestHeader(value = "Referer", required = false) String referer,
                                        RedirectAttributes redirectAttributes) {
        User user = userService.getById(authenticationMetadata.getUserId());
        Product product = productService.getProductById(productId);

        boolean isRemoved = shoppingCartService.removeProduct(user, product);

        redirectAttributes.addFlashAttribute("isRemoved", isRemoved);
        return "redirect:" + referer;
    }
}

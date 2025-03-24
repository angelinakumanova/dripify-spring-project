package com.dripify.web;

import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@CrossOrigin(origins = "localhost:8080")
public class ProductRestController {

    private final ProductService productService;
    private final UserService userService;

    public ProductRestController(ProductService productService, UserService userService) {
        this.productService = productService;
        this.userService = userService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                           @ModelAttribute @Valid CreateProductRequest createProductRequest,
                                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, Map<String, String>> errors = Map.of("errors", bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            error -> error.getField() + "-error",
                            FieldError::getDefaultMessage,
                            (existing, replacement) -> existing
                    )));


            return ResponseEntity.badRequest().body(errors);
        }

        productService.addNewProduct(createProductRequest, userService.getById(authenticationMetadata.getUserId()));

        return ResponseEntity.ok(Map.of("message", "Product registered successfully!"));
    }
}

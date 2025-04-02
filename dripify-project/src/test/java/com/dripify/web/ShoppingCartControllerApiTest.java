package com.dripify.web;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static com.dripify.TestBuilder.principal;
import static com.dripify.TestBuilder.randomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShoppingCartController.class)
public class ShoppingCartControllerApiTest extends BaseApiTest {

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postAuthenticatedRequestToAddProductToCart_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        when(userService.getById(any())).thenReturn(user);

        UUID productId = UUID.randomUUID();
        Product product = Product.builder().id(productId).build();
        when(productService.getProductById(productId)).thenReturn(product);

        String referer = "http://localhost/products/" + productId;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/cart/{productId}", productId)
                .header("Referer", referer)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(referer));
        verify(shoppingCartService, times(1)).addProduct(user, product);
    }

    @Test
    void deleteAuthenticatedRequestToAddProductToCart_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        when(userService.getById(any())).thenReturn(user);

        UUID productId = UUID.randomUUID();
        Product product = Product.builder().id(productId).build();
        when(productService.getProductById(productId)).thenReturn(product);

        String referer = "http://localhost/products/" + productId;

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete("/cart/{productId}", productId)
                .header("Referer", referer)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(referer))
                .andExpect(flash().attributeExists("isRemoved"));
        verify(shoppingCartService, times(1)).removeProduct(user, product);
    }
}

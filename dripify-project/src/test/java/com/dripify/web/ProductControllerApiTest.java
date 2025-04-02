package com.dripify.web;

import com.dripify.category.service.CategoryService;
import com.dripify.product.model.Product;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.web.dto.ProductEditRequest;
import com.dripify.web.dto.ProductFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.UUID;

import static com.dripify.TestBuilder.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerApiTest extends BaseApiTest{

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRequestToGetProductsByGenderAndCategory_shouldReturnProductsView() throws Exception {
        String gender = "men";
        String category = "shirts";
        ProductFilter filter = new ProductFilter();
        Page<Product> productPage = new PageImpl<>(List.of(randomProduct()));

        when(productService.getFilteredProducts(gender, category, null, filter, 0)).thenReturn(productPage);

        mockMvc.perform(get("/products/{gender}/{category}", gender, category)
                        .flashAttr("productFilter", filter))
                .andExpect(status().isOk())
                .andExpect(view().name("/products/products"))
                .andExpect(model().attributeExists("productPage"));
    }

    @Test
    void getRequestToGetProductsByGenderAndSubcategory_shouldReturnProductsView() throws Exception {
        String gender = "women";
        String category = "shirts";
        String subcategory = "formal";
        ProductFilter filter = new ProductFilter();
        Page<Product> productPage = new PageImpl<>(List.of(randomProduct()));
        when(productService.getFilteredProducts(gender, category, subcategory, filter, 0)).thenReturn(productPage);

        mockMvc.perform(get("/products/{gender}/{category}/{subcategory}", gender, category, subcategory)
                        .flashAttr("productFilter", filter))
                .andExpect(status().isOk())
                .andExpect(view().name("/products/products"))
                .andExpect(model().attributeExists("productPage"));
    }

    @Test
    void getRequestToGetNewArrivals_shouldReturnNewArrivalsPage() throws Exception {
        Page<Product> productPage = new PageImpl<>(List.of(randomProduct()));
        when(productService.getNewProducts(0)).thenReturn(productPage);

        mockMvc.perform(get("/products/new-arrivals"))
                .andExpect(status().isOk())
                .andExpect(view().name("/products/new-arrivals"))
                .andExpect(model().attributeExists("productPage"));
    }

    @Test
    void getProduct_shouldReturnSingleProductView() throws Exception {
        UUID productId = UUID.randomUUID();
        Product product = randomProduct();
        when(productService.getProductById(productId)).thenReturn(product);

        mockMvc.perform(get("/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(view().name("/products/single-product"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    void editProduct_shouldRedirectToProductPageOnSuccess() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        UUID productId = UUID.randomUUID();
        ProductEditRequest editRequest = mock(ProductEditRequest.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        MockHttpServletRequestBuilder request = put("/products/{id}/edit", productId)
                .flashAttr("productEditRequest", editRequest)
                .flashAttr("org.springframework.validation.BindingResult.productEditRequest", bindingResult)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + productId));
    }

    @Test
    void updateProductStatus_shouldRedirectToUserProfilePage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        UUID productId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = put("/products/{id}/status", productId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/" + user.getUsername() + "/profile/products"));
    }

    @Test
    void getAuthenticatedRequestToAddNewProductPage_shouldReturnSellProductPage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        MockHttpServletRequestBuilder request = get("/products/new")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("/products/sell-product"));
    }

    @Test
    void favouriteProduct_shouldRedirectToProductPage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        UUID productId = UUID.randomUUID();

        MockHttpServletRequestBuilder request = post("/products/{id}/favourite", productId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products/" + productId));
    }

    @Test
    void removeFavourite_shouldRedirectToRefererPage() throws Exception {
        UUID productId = UUID.randomUUID();
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        String referer = "http://localhost/products/" + productId;

        MockHttpServletRequestBuilder request = delete("/products/{id}/favourite", productId)
                .with(user(principal))
                .header("Referer", referer)
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(referer));
    }



}

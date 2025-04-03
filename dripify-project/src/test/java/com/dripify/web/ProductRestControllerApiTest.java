package com.dripify.web;

import com.dripify.TestBuilder;
import com.dripify.category.model.Category;
import com.dripify.product.model.enums.*;
import com.dripify.product.service.ProductService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.shared.enums.Gender;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.CreateProductRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

import static com.dripify.TestBuilder.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductRestController.class)
public class ProductRestControllerApiTest extends BaseApiTest {

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        CreateProductRequest createProductRequest = createProductRequest();

        when(userService.getById(principal.getUserId())).thenReturn(user);
        doNothing().when(productService).addNewProduct(any(), any());

        mockMvc.perform(post("/api/v1/products/new")
                        .with(user(principal))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("createProductRequest", createProductRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product registered successfully!"));

        verify(productService, times(1)).addNewProduct(any(), any());
    }

    @Test
    void createProduct_invalidInput_returnsBadRequest() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        CreateProductRequest invalidRequest = createProductRequest();
        invalidRequest.setTitle("");

        mockMvc.perform(post("/api/v1/products/new")
                        .with(user(principal))
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .flashAttr("createProductRequest", invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title-error").exists());
    }
}

package com.dripify.web;

import com.dripify.product.service.ProductService;
import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;

import static com.dripify.TestBuilder.principal;
import static com.dripify.TestBuilder.randomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerApiTest extends BaseApiTest {

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthorizedRequestToAllUsers_happyPath() throws Exception {
        User user = randomUser();
        user.setRole(UserRole.ADMIN);
        AuthenticationMetadata principal = principal(user);

        when(userService.getById(user.getId())).thenReturn(user);
        when(userService.getAllUsers(any(), anyInt())).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users")
                .param("page", "0")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("admin-panel"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("currentPath"));
        verify(userService, times(1)).getAllUsers(any(User.class), anyInt());
    }

    @Test
    void getUnauthorizedRequestToAllUsers_shouldReturn404View() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/users")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("404-error-page"));
    }

    @Test
    void putAuthorizedRequestToChangeUserStatus_happyPath() throws Exception {
        User user = randomUser();
        user.setRole(UserRole.ADMIN);
        AuthenticationMetadata principal = principal(user);

        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/users/{id}/status", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).changeStatus(any());
    }

    @Test
    void putAuthorizedRequestToChangeUserRole_happyPath() throws Exception {
        User user = randomUser();
        user.setRole(UserRole.ADMIN);
        AuthenticationMetadata principal = principal(user);

        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/users/{id}/role", UUID.randomUUID())
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"));
        verify(userService, times(1)).switchRole(any());
    }

    @Test
    void getRequestToUserProductsPage_happyPath() throws Exception {
        User user = randomUser();

        when(userService.getByUsername(any())).thenReturn(user);
        when(productService.getProductsByUsername(eq(user), anyInt())).thenReturn(new PageImpl<>(List.of()));
        when(reviewService.getUserReviews(eq(user), anyInt())).thenReturn(new PageImpl<>(List.of()));
        when(orderService.getAllDeliveredOrdersByUserSeller(user)).thenReturn(List.of());

        MockHttpServletRequestBuilder request = get("/users/{username}/profile/products", user.getUsername());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile-items"))
                .andExpect(model().attributeExists("profileUser"))
                .andExpect(model().attributeExists("productPage"))
                .andExpect(model().attributeExists("totalReviews"))
                .andExpect(model().attributeExists("totalOrders"));
        verify(userService, times(1)).getByUsername(user.getUsername());
        verify(productService, times(1)).getProductsByUsername(eq(user), anyInt());
        verify(reviewService, times(1)).getUserReviews(eq(user), anyInt());
        verify(orderService, times(1)).getAllDeliveredOrdersByUserSeller(user);
    }

    @Test
    void getRequestToUserReviewsPage_happyPath() throws Exception {
        User user = randomUser();

        when(userService.getByUsername(any())).thenReturn(user);
        when(reviewService.getUserReviews(eq(user), anyInt())).thenReturn(new PageImpl<>(List.of()));
        when(orderService.getAllDeliveredOrdersByUserSeller(user)).thenReturn(List.of());
        when(productService.getProductsByUsername(eq(user), anyInt())).thenReturn(new PageImpl<>(List.of()));

        MockHttpServletRequestBuilder request = get("/users/{username}/profile/reviews", user.getUsername());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/profile-reviews"))
                .andExpect(model().attributeExists("profileUser"))
                .andExpect(model().attributeExists("reviewsPage"))
                .andExpect(model().attributeExists("totalProducts"))
                .andExpect(model().attributeExists("totalOrders"));
        verify(userService, times(1)).getByUsername(user.getUsername());
        verify(reviewService, times(1)).getUserReviews(eq(user), anyInt());
        verify(orderService, times(1)).getAllDeliveredOrdersByUserSeller(user);
        verify(productService, times(1)).getProductsByUsername(eq(user), anyInt());
    }
}

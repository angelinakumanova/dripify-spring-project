package com.dripify.web;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.UUID;

import static com.dripify.TestBuilder.randomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IndexController.class)
public class IndexControllerApiTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getRequestToIndexEndpoint_shouldReturnIndexView() throws Exception {
        MockHttpServletRequestBuilder request = get("/");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("index"));

    }

    @Test
    void getRequestToAboutUsEndpoint_shouldReturnAboutUsView() throws Exception {
        MockHttpServletRequestBuilder request = get("/about-us");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("about-us"));
    }

    @Test
    void getAuthenticatedRequestToWishlistPage_shouldReturnWishlistView() throws Exception {
        when(userService.getById(any())).thenReturn(randomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/wishlist").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("/user/wishlist"));
        verify(userService, times(1)).getById(userId);
    }

    @Test
    void getUnauthenticatedRequestToWishlistPage_shouldRedirectToLogin() throws Exception {
        MockHttpServletRequestBuilder request = get("/wishlist");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
        verify(userService, never()).getById(any());
    }
}

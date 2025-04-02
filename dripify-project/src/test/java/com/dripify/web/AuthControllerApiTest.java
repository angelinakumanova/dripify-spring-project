package com.dripify.web;

import com.dripify.exception.UserRegistrationException;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
public class AuthControllerApiTest extends BaseApiTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUnauthenticatedRequestToRegister_shouldReturnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = get("/register");


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("registerRequest"))
                .andExpect(view().name("register"));
        verify(userService, never()).getById(any());
    }

    @Test
    void getAuthenticatedRequestToRegister_shouldRedirectToIndexView() throws Exception {
        when(userService.getById(any())).thenReturn(randomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/register").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(userService, times(1)).getById(userId);

    }

    @Test
    void postRequestToRegister_happyPath() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "testUser")
                .formField("firstName", "Test")
                .formField("lastName", "User")
                .formField("email", "test@test.com")
                .formField("password", "123123")
                .formField("confirmPassword", "123123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void postRequestToRegisterEndpointWithInvalidData_returnRegisterView() throws Exception {
        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "")
                .formField("firstName", "Test")
                .formField("lastName", "User")
                .formField("email", "test@test.com")
                .formField("password", "123123")
                .formField("confirmPassword", "123123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
        verify(userService, never()).register(any());

    }

    @Test
    void postRequestToRegisterEndpointWhenUsernameAlreadyExist_thenRedirectToRegisterWithFlashParameter() throws Exception {
        doThrow(new UserRegistrationException("username", "Username is already in use"))
                .when(userService).register(any());

        MockHttpServletRequestBuilder request = post("/register")
                .formField("username", "testUser")
                .formField("firstName", "Test")
                .formField("lastName", "User")
                .formField("email", "test@test.com")
                .formField("password", "123123")
                .formField("confirmPassword", "123123")
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register"))
                .andExpect(flash().attributeExists("usernameError"));
        verify(userService, times(1)).register(any());
    }

    @Test
    void getUnauthenticatedRequestToLogin_shouldReturnLoginView() throws Exception {
        MockHttpServletRequestBuilder request = get("/login");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
        verify(userService, never()).getById(any());
    }

    @Test
    void getRequestToLoginEndpointWithErrorParameter_shouldReturnLoginViewAndErrorMessageAttribute() throws Exception {
        MockHttpServletRequestBuilder request = get("/login").param("error", "");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
        verify(userService, never()).getById(any());
    }

    @Test
    void getAuthenticatedRequestToLogin_shouldRedirectToIndexView() throws Exception {
        when(userService.getById(any())).thenReturn(randomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/login").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
        verify(userService, times(1)).getById(userId);
    }
}

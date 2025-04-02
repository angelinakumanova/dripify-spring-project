package com.dripify.web;

import com.dripify.notification.client.dto.NotificationPreference;
import com.dripify.notification.service.NotificationService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.web.dto.PasswordUpdateRequest;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.dto.UsernameUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static com.dripify.TestBuilder.principal;
import static com.dripify.TestBuilder.randomUser;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileSettingsController.class)
public class ProfileSettingsControllerApiTest extends BaseApiTest {

    @MockitoBean
    private NotificationService notificationService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void getAuthenticatedRequestToSettingsPage_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(notificationService.getNotificationPreference(user.getId())).thenReturn(new NotificationPreference());
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/settings/profile")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/account-settings"))
                .andExpect(model().attributeExists("passwordUpdateRequest"))
                .andExpect(model().attributeExists("usernameUpdateRequest"))
                .andExpect(model().attributeExists("emailUpdateRequest"));
        verify(notificationService, times(1)).getNotificationPreference(user.getId());
        verify(userService, times(2)).getById(user.getId());
    }

    @Test
    void getAuthenticatedRequestToEditPage_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/settings/profile/edit")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("/user/edit-profile"))
                .andExpect(model().attributeExists("userEditRequest"));
    }

    @Test
    void putAuthenticatedRequestToEditProfilePage_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = put("/settings/profile/edit")
                .formField("firstName", "Test")
                .formField("lastName", "User")
                .formField("description", "Description")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile/edit"))
                .andExpect(flash().attributeExists("successMessage"));
        verify(userService, times(1)).editProfile(any(), eq(user));
    }

    @Test
    void putAuthenticatedRequestToEditPageWithInvalidData_shouldReturnEditProfileView() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = put("/settings/profile/edit")
                .formField("firstName", "")
                .formField("lastName", "User")
                .formField("description", "Description")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/edit-profile"));
        verify(userService, never()).editProfile(any(), any());
    }

    @Test
    void putAuthenticatedRequestToUsernameEdit_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = put("/settings/profile/username")
                .formField("username", "userTest")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("successUsernameMessage"));
        verify(userService, times(1)).updateUsername(eq(user), anyString());
    }

    @Test
    void putAuthenticatedRequestToUsernameEditWithInvalidData_shouldRedirectToSettingsProfilePage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/settings/profile/username")
                .formField("username", "")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("emailUpdateRequest"))
                .andExpect(flash().attributeExists("usernameUpdateRequest"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.usernameUpdateRequest"));
        verify(userService, never()).updateUsername(any(), anyString());
    }

    @Test
    void putAuthenticatedRequestToEmailEdit_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = put("/settings/profile/email")
                .formField("email", "test@example.com")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("successEmailMessage"));
        verify(userService, times(1)).updateEmail(eq(user), anyString());
    }

    @Test
    void putAuthenticatedRequestToEmailEditWithInvalidData_shouldRedirectToSettingsProfilePage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/settings/profile/email")
                .formField("email", "")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("emailUpdateRequest"))
                .andExpect(flash().attributeExists("usernameUpdateRequest"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.emailUpdateRequest"));
        verify(userService, never()).updateEmail(any(), anyString());
    }

    @Test
    void putAuthenticatedRequestToPasswordEdit_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);


        MockHttpServletRequestBuilder request = put("/settings/profile/password")
                .formField("password", "123456")
                .formField("confirmPassword", "123456")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("successPasswordMessage"));
        verify(userService, times(1)).updatePassword(eq(user), anyString());
    }

    @Test
    void putAuthenticatedRequestToPasswordEditWithInvalidData_shouldRedirectToSettingsProfilePage() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/settings/profile/password")
                .formField("password", "")
                .formField("confirmPassword", "")
                .with(user(principal))
                .with(csrf());


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"))
                .andExpect(flash().attributeExists("passwordUpdateRequest"))
                .andExpect(flash().attributeExists("usernameUpdateRequest"))
                .andExpect(flash().attributeExists("emailUpdateRequest"))
                .andExpect(flash().attributeExists("org.springframework.validation.BindingResult.passwordUpdateRequest"));
        verify(userService, never()).updatePassword(any(), anyString());
    }

    @Test
    void putAuthenticatedRequestToUserStatus_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(user.getId())).thenReturn(user);

        MockHttpServletRequestBuilder request = put("/settings/profile/user-status")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attributeExists("successUserDeactivation"));
        verify(userService, times(1)).changeStatus(user);

    }

    @Test
    void putAuthenticatedRequestToNotificationsPreference_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);

        MockHttpServletRequestBuilder request = put("/settings/profile/notifications/preference")
                .param("enabled", "true")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/settings/profile"));
        verify(notificationService, times(1)).updateNotificationPreference(eq(principal.getUserId()), anyBoolean());
    }
}

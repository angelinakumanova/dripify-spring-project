package com.dripify.web;

import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.dripify.TestBuilder.principal;
import static com.dripify.TestBuilder.randomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReviewController.class)
public class ReviewControllerApiTest extends BaseApiTest {

    @MockitoBean
    private ReviewService reviewService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToCreateReview_happyPath() throws Exception {
        User user = randomUser();
        AuthenticationMetadata principal = principal(user);
        when(userService.getById(any())).thenReturn(user);
        when(userService.getByUsername(any())).thenReturn(new User());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/reviews/{username}", "revieweeUser")
                .param("orderId", "2")
                .with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/order-review"))
                .andExpect(model().attributeExists("username"))
                .andExpect(model().attributeExists("orderId"))
                .andExpect(model().attributeExists("reviewCreateRequest"));
        verify(userService, times(2)).getById(any());
        verify(userService, times(1)).getByUsername(any());
        verify(orderService, times(1)).validateOrderAccess(any(), any(), any());
    }

    @Test
    void postAuthenticatedRequestToCreateReview_happyPath() throws Exception {
        User reviewer = randomUser();
        AuthenticationMetadata principal = principal(reviewer);
        when(userService.getById(any())).thenReturn(reviewer);
        User reviewee = User.builder().username("revieweeUser").build();
        when(userService.getByUsername(any())).thenReturn(reviewee);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/reviews/{username}", reviewee.getUsername())
                .param("orderId", "2")
                .formField("title", "test title")
                .formField("comment", "this is a test comment")
                .formField("rating", "5")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/purchases"));
        verify(userService, times(2)).getById(any());
        verify(userService, times(1)).getByUsername(any());
        verify(reviewService, times(1)).createNewReview(any(), eq(reviewer), eq(reviewee), any());
    }

    @Test
    void postAuthenticatedRequestToCreateReviewWithInvalidData_shouldReturnOrderReviewView() throws Exception {
        User reviewer = randomUser();
        AuthenticationMetadata principal = principal(reviewer);
        when(userService.getById(any())).thenReturn(reviewer);
        User reviewee = User.builder().username("revieweeUser").build();
        when(userService.getByUsername(any())).thenReturn(reviewee);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/reviews/{username}", reviewee.getUsername())
                .param("orderId", "2")
                .formField("title", "")
                .formField("comment", "this is a test comment")
                .formField("rating", "5")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/order-review"));
        verify(userService, never()).getByUsername(any());
        verify(reviewService, never()).createNewReview(any(), any(), any(), any());
    }
}

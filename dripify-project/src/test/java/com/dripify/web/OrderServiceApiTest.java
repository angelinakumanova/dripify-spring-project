package com.dripify.web;

import com.dripify.cart.service.ShoppingCartService;
import com.dripify.order.model.Order;
import com.dripify.order.model.OrderStatus;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.UUID;

import static com.dripify.TestBuilder.randomUser;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
public class OrderServiceApiTest extends BaseApiTest {

    @MockitoBean
    private ShoppingCartService shoppingCartService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAuthenticatedRequestToPurchases_shouldReturnPurchasesView() throws Exception {
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/orders/purchases").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/orders-purchases"))
                .andExpect(model().attributeExists("ordersByDate"));
        verify(orderService, times(1)).getPurchasedByUser(user);
    }

    @Test
    void getUnauthenticatedRequestToPurchases_shouldRedirectToLoginPage() throws Exception {
        MockHttpServletRequestBuilder request = get("/orders/purchases");


        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getAuthenticatedRequestToSales_shouldReturnPurchasesView() throws Exception {
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/orders/sales").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("user/orders-sales"))
                .andExpect(model().attributeExists("ordersByDate"));
        verify(orderService, times(1)).getSoldByUser(user);
    }

    @Test
    void getUnauthenticatedRequestToSales_shouldRedirectToLoginPage() throws Exception {
        MockHttpServletRequestBuilder request = get("/orders/sales");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getUnauthenticatedRequestToCheckout_shouldRedirectToLoginPage() throws Exception {
        MockHttpServletRequestBuilder request = get("/orders/checkout");

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    void getAuthenticatedRequestAndEmptyShoppingCartToCheckout_shouldReturn404ErrorView() throws Exception {
        User user = randomUser();

        when(userService.getById(any())).thenReturn(user);
        doThrow(AccessDeniedException.class).when(shoppingCartService).validateCartNotEmpty(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = get("/orders/checkout").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isNotFound())
                .andExpect(view().name("404-error-page"));
    }

    @Test
    void getAuthenticatedRequestAndShoppingCartWithAtLeastOneProductToCheckout_shouldReturnCheckoutView() throws Exception {
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);
        doNothing().when(shoppingCartService).validateCartNotEmpty(user);

        MockHttpServletRequestBuilder request = get("/orders/checkout").with(user(principal));

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"))
                .andExpect(model().attributeExists("orderCreateRequest"));
        verify(userService, times(2)).getById(userId);
    }

    @Test
    void postAuthenticatedRequestToCheckout_happyPath() throws Exception {
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/orders/checkout")
                .formField("purchaserFullName", "Test User")
                .formField("purchaserAddress", "Test Address")
                .formField("country", "BULGARIA")
                .formField("city", "SOFIA")
                .formField("purchaserPhoneNumber", "890 355 355")
                .formField("orderPayment", "CARD")
                .formField("orderDeliveryCourier", "SPEEDY")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/completed"))
                .andExpect(flash().attributeExists("orders"));
    }

    @Test
    void postAuthenticatedRequestWithInvalidDataToCheckout_shouldReturnCheckoutView() throws Exception {
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = post("/orders/checkout")
                .formField("purchaserFullName", "")
                .formField("purchaserAddress", "Test Address")
                .formField("country", "")
                .formField("city", "SOFIA")
                .formField("purchaserPhoneNumber", "890 355 355")
                .formField("orderPayment", "CARD")
                .formField("orderDeliveryCourier", "SPEEDY")
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(view().name("checkout"));
        verify(orderService, never()).createNewOrder(any(), any());
    }

    @Test
    void getAuthenticatedRequestToOrderCompletedPage_WhenNoOrders_ShouldRedirectToPurchases() throws Exception {
        when(userService.getById(any())).thenReturn(randomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        mockMvc.perform(get("/orders/completed").with(user(principal)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/purchases"));
    }

    @Test
    void getOrderCompletedPage_WhenOrdersExist_ShouldReturnOrderCompletedView() throws Exception {
        when(userService.getById(any())).thenReturn(randomUser());

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        mockMvc.perform(get("/orders/completed")
                        .flashAttr("orders", List.of(new Order())).with(user(principal)))
                .andExpect(status().isOk())
                .andExpect(view().name("order-completed"));
    }

    @Test
    void putRequestToModifyOrderStatusForSeller_ShouldRedirectToSalesWithOrderId() throws Exception {
        Long orderId = 1L;
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = put("/orders/sales/status/{id}", orderId)
                                                .with(user(principal))
                                                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/sales#order-" + orderId));

        verify(orderService, times(1)).changeOrderStatus(user, orderId, OrderStatus.SHIPPED);
    }

    @Test
    void modifyOrderStatusForPurchaser_ShouldRedirectToPurchasesWithOrderId() throws Exception {
        Long orderId = 1L;
        User user = randomUser();
        when(userService.getById(any())).thenReturn(user);

        UUID userId = UUID.randomUUID();
        AuthenticationMetadata principal =
                new AuthenticationMetadata(userId, "testUser", "123123", UserRole.USER, true);

        MockHttpServletRequestBuilder request = put("/orders/purchases/status/{id}", orderId)
                .with(user(principal))
                .with(csrf());

        mockMvc.perform(request)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/purchases#order-" + orderId));

        verify(orderService, times(1)).changeOrderStatus(user, orderId, OrderStatus.DELIVERED);
    }
}

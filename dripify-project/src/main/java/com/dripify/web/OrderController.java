package com.dripify.web;

import com.dripify.order.model.Order;
import com.dripify.order.model.OrderStatus;
import com.dripify.order.service.OrderService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.OrderCreateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }


    @GetMapping("/purchases")
    public ModelAndView getsOrdersPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("/user/orders-purchases");

        User user = userService.getById(authenticationMetadata.getUserId());
        Map<LocalDate, List<Order>> userOrdersGroupedByDate = orderService.getPurchasedByUser(user);
        modelAndView.addObject("ordersByDate", userOrdersGroupedByDate);


        return modelAndView;
    }

    @GetMapping("/sales")
    public ModelAndView getsSalesPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("/user/orders-sales");

        User user = userService.getById(authenticationMetadata.getUserId());
        Map<LocalDate, List<Order>> sellerOrdersGroupedByDate = orderService.getSoldByUser(user);
        modelAndView.addObject("ordersByDate", sellerOrdersGroupedByDate);

        return modelAndView;
    }

    @GetMapping("/checkout")
    public ModelAndView getCheckoutPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        if (user.getShoppingCart().getProducts().isEmpty()) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView modelAndView = new ModelAndView("checkout");
        modelAndView.addObject("orderCreateRequest", new OrderCreateRequest());

        return modelAndView;
    }

    @PostMapping("/checkout")
    public String postCheckout(@Valid OrderCreateRequest orderCreateRequest, BindingResult bindingResult,
                               @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                               RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            return "checkout";
        }

        User user = userService.getById(authenticationMetadata.getUserId());

        List<Order> orders = orderService.createNewOrder(orderCreateRequest, user);

        redirectAttributes.addFlashAttribute("orders", orders);

        return "redirect:/orders/completed";
    }


    @GetMapping("/completed")
    public String getOrderCompletedPage(Model model) {

        if (!model.containsAttribute("orders")) {
            return "redirect:/orders/purchases";
        }

        return "order-completed";
    }

    @PutMapping("/sales/status/{id}")
    public String modifyOrderStatusForSeller (@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                     @PathVariable(value = "id") Long orderId) {
        User user = userService.getById(authenticationMetadata.getUserId());

        orderService.changeOrderStatus(user, orderId, OrderStatus.SHIPPED);

        return "redirect:/orders/sales" + "#order-" + orderId;
    }

    @PutMapping("/purchases/status/{id}")
    public String modifyOrderStatusForPurchaser (@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                     @PathVariable(value = "id") Long orderId) {
        User user = userService.getById(authenticationMetadata.getUserId());

        orderService.changeOrderStatus(user, orderId, OrderStatus.DELIVERED);

        return "redirect:/orders/purchases" + "#order-" + orderId;
    }
}

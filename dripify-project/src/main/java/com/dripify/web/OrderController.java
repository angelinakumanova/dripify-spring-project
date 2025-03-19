package com.dripify.web;

import com.dripify.order.model.Order;
import com.dripify.order.service.OrderService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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
        List<Order> purchasedByUser = orderService.getPurchasedByUser(user);
        modelAndView.addObject("purchasedOrders", purchasedByUser);


        return modelAndView;
    }

    @GetMapping("/checkout")
    public String getCheckoutPage() {

        return "checkout";
    }
}

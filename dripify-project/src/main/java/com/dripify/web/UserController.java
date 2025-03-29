package com.dripify.web;

import com.dripify.order.service.OrderService;
import com.dripify.product.service.ProductService;
import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.UUID;


@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final ReviewService reviewService;
    private final OrderService orderService;

    public UserController(UserService userService, ProductService productService, ReviewService reviewService, OrderService orderService) {
        this.userService = userService;
        this.productService = productService;
        this.reviewService = reviewService;
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ModelAndView getAllUsers(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata,
                                    @RequestParam(defaultValue = "0") int page,
                                    HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("admin-panel");

        User user = userService.getById(authenticationMetadata.getUserId());
        Page<User> allUsers = userService.getAllUsers(user, page);

        modelAndView.addObject("users", allUsers);
        modelAndView.addObject("currentPath", request.getRequestURI());

        return modelAndView;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public String updateUserStatus(@PathVariable(value = "id") UUID targetUserId) {
        User targetUser = userService.getById(targetUserId);

        userService.changeStatus(targetUser);

        return "redirect:/users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/role")
    public String updateUserRole(@PathVariable(value = "id") UUID targetUserId) {
        User targetUser = userService.getById(targetUserId);

        userService.switchRole(targetUser);

        return "redirect:/users";
    }

    @GetMapping("/{username}/profile/products")
    public ModelAndView getUserProductsPage(@PathVariable String username,
                                       @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("user/profile-items");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);


        modelAndView.addObject("productPage", productService.getProductsByUsername(profileUser, page));
        modelAndView.addObject("totalReviews", reviewService.getUserReviews(profileUser, 0).getTotalElements());
        modelAndView.addObject("totalOrders", orderService.getAllDeliveredOrdersByUserSeller(profileUser).size());

        return modelAndView;
    }

    @GetMapping("/{username}/profile/reviews")
    public ModelAndView getUserReviewsPage(@PathVariable String username,
                                           @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("user/profile-reviews");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);

        modelAndView.addObject("reviewsPage", reviewService.getUserReviews(profileUser, page));
        modelAndView.addObject("totalProducts", productService.getProductsByUsername(profileUser, 0).getTotalElements());
        modelAndView.addObject("totalOrders", orderService.getAllDeliveredOrdersByUserSeller(profileUser).size());


        return modelAndView;
    }



}

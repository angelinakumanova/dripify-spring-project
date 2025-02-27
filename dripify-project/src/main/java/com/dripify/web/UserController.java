package com.dripify.web;

import com.dripify.order.service.OrderService;
import com.dripify.product.service.ProductService;
import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


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

    @GetMapping("/{username}/profile/products")
    public ModelAndView getUserProductsPage(@PathVariable String username,
                                       @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("user/profile-items");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);

        modelAndView.addObject("productPage", productService.getProductsByUsername(username, page));
        modelAndView.addObject("totalReviews", reviewService.getUserReviews(profileUser, 0).getTotalElements());
        modelAndView.addObject("totalOrders", orderService.getAllDeliveredOrdersByUser(profileUser).size());

        return modelAndView;
    }

    @GetMapping("/{username}/profile/reviews")
    public ModelAndView getUserReviewsPage(@PathVariable String username,
                                           @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("user/profile-reviews");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);

        modelAndView.addObject("reviewsPage", reviewService.getUserReviews(profileUser, page));
        modelAndView.addObject("totalProducts", productService.getProductsByUsername(username, 0).getTotalElements());
        modelAndView.addObject("totalOrders", orderService.getAllDeliveredOrdersByUser(profileUser).size());


        return modelAndView;
    }



}

package com.dripify.web;

import com.dripify.order.service.OrderService;
import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.ReviewCreateRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final OrderService orderService;

    public ReviewController(ReviewService reviewService, UserService userService, OrderService orderService) {
        this.reviewService = reviewService;
        this.userService = userService;
        this.orderService = orderService;
    }

    @GetMapping("/{username}")
    public ModelAndView getCreateReviewPage(@PathVariable String username, @RequestParam Long orderId,
                                            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User purchaser = userService.getById(authenticationMetadata.getUserId());
        User seller = userService.getByUsername(username);

        orderService.validateOrderAccess(seller, purchaser, orderId);

        ModelAndView modelAndView = new ModelAndView("user/order-review");
        modelAndView.addObject("username", username);
        modelAndView.addObject("orderId", orderId);
        modelAndView.addObject("reviewCreateRequest", new ReviewCreateRequest());


        return modelAndView;
    }

    @PostMapping("/{username}")
    public String addReview(@PathVariable String username, @RequestParam Long orderId,
                            @Valid ReviewCreateRequest reviewCreateRequest,
                            BindingResult bindingResult,
                            @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {

            return "user/order-review";
        }

        User reviewee = userService.getByUsername(username);
        User reviewer = userService.getById(authenticationMetadata.getUserId());

        reviewService.createNewReview(orderId, reviewer, reviewee, reviewCreateRequest);


        return "redirect:/orders/purchases";
    }
}

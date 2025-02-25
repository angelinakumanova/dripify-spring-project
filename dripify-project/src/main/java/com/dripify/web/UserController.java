package com.dripify.web;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.product.service.ProductService;
import com.dripify.review.service.ReviewService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public UserController(UserService userService, ProductService productService, CategoryService categoryService, ReviewService reviewService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getMainCategories();
    }

    @ModelAttribute("user")
    public User getUser(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (authenticationMetadata != null) {
            return userService.getById(authenticationMetadata.getUserId());
        }

        return null;
    }

    @GetMapping("/{username}/profile/products")
    public ModelAndView getUserProductsPage(@PathVariable String username,
                                       @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/user/profile-items");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);

        modelAndView.addObject("productPage", productService.getProductsByUsername(username, page));
        modelAndView.addObject("totalReviews", reviewService.getUserReviews(profileUser, 0).getTotalElements());

        return modelAndView;
    }

    @GetMapping("/{username}/profile/reviews")
    public ModelAndView getUserReviewsPage(@PathVariable String username,
                                           @RequestParam(defaultValue = "0") int page) {
        ModelAndView modelAndView = new ModelAndView("/user/profile-reviews");

        User profileUser = userService.getByUsername(username);
        modelAndView.addObject("profileUser", profileUser);

        modelAndView.addObject("reviewsPage", reviewService.getUserReviews(profileUser, page));
        modelAndView.addObject("totalProducts", productService.getProductsByUsername(username, 0).getTotalElements());

        return modelAndView;
    }

    @GetMapping("/{username}/profile/edit")
    public String editUserProfilePage(@PathVariable String username, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (!username.equalsIgnoreCase(authenticationMetadata.getUsername())) {
            return "redirect:/";
        }

        return "user/edit-profile";
    }


}

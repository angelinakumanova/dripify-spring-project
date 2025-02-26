package com.dripify.web;

import com.dripify.category.model.Category;
import com.dripify.category.service.CategoryService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final CategoryService categoryService;

    public ProfileController(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
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


    @GetMapping("/edit")
    public ModelAndView getEditProfilePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/edit-profile");
        modelAndView.addObject("userEditRequest", DtoMapper.mapUserToEditRequest(userService.getById(authenticationMetadata.getUserId())));

        return modelAndView;
    }

    @PutMapping("/edit")
    public String editUserProfile(@Valid UserEditRequest userEditRequest,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes,
                                  @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            return "user/edit-profile";
        }

        userService.editUserProfile(userEditRequest, userService.getById(authenticationMetadata.getUserId()));
        redirectAttributes.addFlashAttribute("successMessage", "Successfully changed!");

        return "redirect:/profile/edit";
    }


    @GetMapping("/{username}/profile/settings")
    public String getSettingsPage(@PathVariable String username, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (!username.equalsIgnoreCase(authenticationMetadata.getUsername())) {
            return "redirect:/";
        }

        return "user/account-settings";
    }

    @GetMapping("/{username}/orders")
    public String getOrdersPage(@PathVariable String username, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        if (!username.equalsIgnoreCase(authenticationMetadata.getUsername())) {
            return "redirect:/";
        }

        return "user/orders";
    }
}

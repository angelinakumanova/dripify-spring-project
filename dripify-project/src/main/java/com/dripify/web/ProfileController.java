package com.dripify.web;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.dto.UsernameUpdateRequest;
import com.dripify.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
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


    @GetMapping("/settings")
    public ModelAndView getSettingsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("user/account-settings");

        User user = userService.getById(authenticationMetadata.getUserId());
        modelAndView.addObject("usernameUpdateRequest", DtoMapper.mapToUsernameUpdateRequest(user));
        modelAndView.addObject("emailUpdateRequest", DtoMapper.mapToEmailUpdateRequest(user));

        return modelAndView;
    }

    @PutMapping("/settings/update-username")
    public String updateUsername(@Valid UsernameUpdateRequest usernameUpdateRequest,
                                       BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes, @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (bindingResult.hasErrors()) {
            return "user/account-settings";
        }

        userService.updateUsername(userService.getById(authenticationMetadata.getUserId()), usernameUpdateRequest.getUsername());
        redirectAttributes.addFlashAttribute("successMessage", "Username is successfully changed!");

        return "redirect:/profile/settings";
    }
}

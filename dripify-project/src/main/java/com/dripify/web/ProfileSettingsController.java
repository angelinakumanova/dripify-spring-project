package com.dripify.web;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.EmailUpdateRequest;
import com.dripify.web.dto.PasswordUpdateRequest;
import com.dripify.web.dto.UserEditRequest;
import com.dripify.web.dto.UsernameUpdateRequest;
import com.dripify.web.mapper.DtoMapper;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/settings/profile")
public class ProfileSettingsController {

    private final UserService userService;

    public ProfileSettingsController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/edit")
    public ModelAndView getEditProfilePage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("/user/edit-profile");
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

        userService.editProfile(userEditRequest, userService.getById(authenticationMetadata.getUserId()));
        redirectAttributes.addFlashAttribute("successMessage", "Successfully changed!");

        return "redirect:/profile/edit";
    }


    @GetMapping
    public String getSettingsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata, Model model) {

        if (!model.containsAttribute("passwordUpdateRequest")) {
            model.addAttribute("passwordUpdateRequest", new PasswordUpdateRequest());
        }
        
        if (!model.containsAttribute("usernameUpdateRequest") && !model.containsAttribute("emailUpdateRequest")) {
            User user = userService.getById(authenticationMetadata.getUserId());

            model.addAttribute("usernameUpdateRequest", DtoMapper.mapToUsernameUpdateRequest(user));
            model.addAttribute("emailUpdateRequest", DtoMapper.mapToEmailUpdateRequest(user));
        }

        return "user/account-settings";
    }

    @PutMapping("/username")
    public String updateUsername(@Valid UsernameUpdateRequest usernameUpdateRequest,
                                 BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailUpdateRequest", DtoMapper.mapToEmailUpdateRequest(user));
            redirectAttributes.addFlashAttribute("usernameUpdateRequest", usernameUpdateRequest);

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.usernameUpdateRequest", bindingResult);
            return "redirect:/settings/profile";
        }

        userService.updateUsername(user, usernameUpdateRequest.getUsername());
        redirectAttributes.addFlashAttribute("successUsernameMessage", "Username successfully changed!");

        return "redirect:/settings/profile";
    }

    @PutMapping("/email")
    public String updateEmail(@Valid EmailUpdateRequest emailUpdateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("emailUpdateRequest", emailUpdateRequest);
            redirectAttributes.addFlashAttribute("usernameUpdateRequest", DtoMapper.mapToUsernameUpdateRequest(user));

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.emailUpdateRequest", bindingResult);
            return "redirect:/settings/profile";
        }

        userService.updateEmail(user, emailUpdateRequest.getEmail());
        redirectAttributes.addFlashAttribute("successEmailMessage", "Email successfully changed!");
        return "redirect:/settings/profile";
    }

    @PutMapping("/password")
    public String updatePassword(@Valid PasswordUpdateRequest passwordUpdateRequest, BindingResult bindingResult, RedirectAttributes redirectAttributes,
                                 @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        User user = userService.getById(authenticationMetadata.getUserId());

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("passwordUpdateRequest", passwordUpdateRequest);
            redirectAttributes.addFlashAttribute("usernameUpdateRequest", DtoMapper.mapToUsernameUpdateRequest(user));
            redirectAttributes.addFlashAttribute("emailUpdateRequest", DtoMapper.mapToEmailUpdateRequest(user));

            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.passwordUpdateRequest", bindingResult);

            return "redirect:/settings/profile";
        }

        userService.updatePassword(user, passwordUpdateRequest.getPassword());
        redirectAttributes.addFlashAttribute("successPasswordMessage", "Password successfully changed!");

        return "redirect:/settings/profile";
    }

    @PutMapping("/active")
    public String deactivateProfile(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        userService.deactivateUser(userService.getById(authenticationMetadata.getUserId()));

        //TODO: Redirect to thank you page
        return "redirect:/";
    }
}

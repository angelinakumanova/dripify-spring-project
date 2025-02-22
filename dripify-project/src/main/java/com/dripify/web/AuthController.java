package com.dripify.web;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.service.UserService;
import com.dripify.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public ModelAndView getRegisterPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {

        if (authenticationMetadata != null) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mav = new ModelAndView("register");
        mav.addObject("registerRequest", new RegisterRequest());

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView getLoginPage(@RequestParam(required = false, value = "error") String errorParam,
                                     @AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("login");

        if (authenticationMetadata != null) {
            return new ModelAndView("redirect:/");
        }

        if (errorParam != null) {
            modelAndView.addObject("error", "Incorrect username or password");
        }

        return modelAndView;
    }

    @PostMapping("/register")
    public String registerPost(@Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            return "register";
        }

        userService.register(registerRequest);

        return "redirect:/login";
    }
}

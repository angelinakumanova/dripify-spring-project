package com.dripify.web;

import com.dripify.user.service.UserService;
import com.dripify.web.dto.LoginRequest;
import com.dripify.web.dto.RegisterRequest;
import jakarta.validation.Valid;
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
    public ModelAndView getRegister() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("registerRequest", new RegisterRequest());

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("loginRequest", new LoginRequest());

        return mav;
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

    @PostMapping("/login")
    public String loginPost(@Valid LoginRequest loginRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "login";
        }

        return "redirect:/";
    }



    @GetMapping("/register/validate-username")
    @ResponseBody
    public boolean checkUsernameExists(@RequestParam String username) {
        return userService.existsByUsername(username);
    }
}

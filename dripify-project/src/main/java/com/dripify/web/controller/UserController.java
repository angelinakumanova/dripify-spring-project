package com.dripify.web.controller;

import com.dripify.user.service.UserService;
import com.dripify.web.dto.LoginRequest;
import com.dripify.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/register/validate-username")
    @ResponseBody
    public boolean checkUsernameExists(@RequestParam String username) {
        return userService.existsByUsername(username);
    }

    @PostMapping("/register")
    public ModelAndView registerPost(@Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return new ModelAndView("register");
        }

        //TODO: REGISTER USER

        return new ModelAndView("redirect:/");
    }

    @PostMapping("/login")
    public ModelAndView loginPost(LoginRequest loginRequest, RedirectAttributes rAtt) {

        if (!this.userService.existsByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword())) {
            rAtt.addFlashAttribute("userExists", false);

            return new ModelAndView("redirect:/login");
        }

        return new ModelAndView("redirect:/");
    }
}

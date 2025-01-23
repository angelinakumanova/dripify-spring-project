package com.dripify.web.controller;

import com.dripify.user.service.UserService;
import com.dripify.web.dto.LoginRequest;
import com.dripify.web.dto.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegister")
    public RegisterRequest userRegister() {
        return new RegisterRequest();
    }

    @ModelAttribute("userLogin")
    public LoginRequest userLogin() {
        return new LoginRequest();
    }

    @GetMapping("/register")
    public String getRegister() {
        return "register";
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }


    @PostMapping("/register")
    public ModelAndView registerPost(@Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult, RedirectAttributes rAtt) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");

        if (userService.getUserByUsername(registerRequest.getUsername()) == null) {
            bindingResult.rejectValue("username", "username.exists", "Username is already taken.");
        }

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("userRegister", registerRequest);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userRegister", bindingResult);

            return new ModelAndView("redirect:/register");
        }

        return modelAndView;
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

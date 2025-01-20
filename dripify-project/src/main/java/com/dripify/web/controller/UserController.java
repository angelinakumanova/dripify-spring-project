package com.dripify.web.controller;

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

    @ModelAttribute("userRegister")
    public RegisterRequest userRegister() {
        // Initializes the RegisterRequest object for the model
        return new RegisterRequest();
    }
    @GetMapping("/register")
    public String getRegister() {

        return "register";
    }


    @PostMapping("/register")
    public ModelAndView registerPost(@Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult, RedirectAttributes rAtt) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");

        if (bindingResult.hasErrors()) {
            rAtt.addFlashAttribute("isTaken", true);
            rAtt.addFlashAttribute("areMatching", registerRequest.getPassword().equals(registerRequest.getConfirmPassword()));
            rAtt.addFlashAttribute("userRegister", registerRequest);
            rAtt.addFlashAttribute("org.springframework.validation.BindingResult.userRegister", bindingResult);

            return new ModelAndView("redirect:/register");
        }

        return modelAndView;
    }
}

package com.dripify.web.controller;

import com.dripify.web.dto.LoginRequest;
import com.dripify.web.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @GetMapping
    public String getIndex() {
        return "index";
    }

    @GetMapping("/register")
    public ModelAndView getRegister() {
        ModelAndView mav = new ModelAndView("register");
        mav.addObject("userRegister", new RegisterRequest());

        return mav;
    }

    @GetMapping("/login")
    public ModelAndView getLogin() {
        ModelAndView mav = new ModelAndView("login");
        mav.addObject("userLogin", new LoginRequest());

        return mav;
    }
}

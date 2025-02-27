package com.dripify.web;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController {

    private final UserService userService;

    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getIndexPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("index");

        if (authenticationMetadata != null) {
            User user = userService.getById(authenticationMetadata.getUserId());
            modelAndView.addObject("user", user);
        }


        return modelAndView;
    }

    @GetMapping("/about-us")
    public ModelAndView getAboutUsPage(@AuthenticationPrincipal AuthenticationMetadata authenticationMetadata) {
        ModelAndView modelAndView = new ModelAndView("about-us");

        if (authenticationMetadata != null) {
            User user = userService.getById(authenticationMetadata.getUserId());
            modelAndView.addObject("user", user);
        }

        return modelAndView;
    }
}

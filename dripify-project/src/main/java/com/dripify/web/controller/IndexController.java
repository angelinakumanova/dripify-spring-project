package com.dripify.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping
    public String getIndexPage() {
        return "index";
    }

    @GetMapping("/about-us")
    public String getAboutUsPage() {

        return "about-us";
    }

    @GetMapping("/single-product")
    public String getSingleProductPage() {
        return "/products/single-product";
    }

}

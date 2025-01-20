package com.dripify.web.controller;

import com.dripify.web.dto.RegisterRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class IndexController {

    @GetMapping
    public String getIndex() {
        return "index";
    }


}

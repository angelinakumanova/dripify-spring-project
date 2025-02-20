package com.dripify.web;

import com.dripify.category.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    private final CategoryService categoryService;

    public IndexController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ModelAndView getIndexPage() {
        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("categories", categoryService.getMainCategories());

        return modelAndView;
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

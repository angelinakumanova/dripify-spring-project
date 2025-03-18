package com.dripify.web.interceptor;

import com.dripify.category.service.CategoryService;
import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.model.User;
import com.dripify.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ModelAttributeInterceptor implements HandlerInterceptor {

    private final UserService userService;
    private final CategoryService categoryService;

    public ModelAttributeInterceptor(UserService userService, CategoryService categoryService) {
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof AuthenticationMetadata authenticationMetadata) {
            User user = userService.getById(authenticationMetadata.getUserId());

            if (modelAndView != null) {
                modelAndView.addObject("user", user);
            }
        }


        if (modelAndView != null) {
            modelAndView.addObject("categories", categoryService.getMainCategories());
        }
    }
}

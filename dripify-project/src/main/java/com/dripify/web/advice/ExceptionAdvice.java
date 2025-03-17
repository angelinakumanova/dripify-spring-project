package com.dripify.web.advice;

import com.dripify.exception.UserRegistrationException;
import com.dripify.exception.UserUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(UserUpdateException.class)
    public String handleUserUpdateException(UserUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(e.getField() + "Error", e.getMessage());

        return "redirect:/settings/profile";
    }

    @ExceptionHandler(UserRegistrationException.class)
    public String handleUserRegistrationException(UserRegistrationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(e.getField() + "Error", e.getMessage());

        return "redirect:/register";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({IllegalArgumentException.class, NoResourceFoundException.class, MethodArgumentTypeMismatchException.class})
    public ModelAndView handleNotFoundException(Exception e) {

        return new ModelAndView("404-error-page");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ModelAndView handleAnyException(Exception exception) {

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("internal-server-error");

        return modelAndView;
    }


}

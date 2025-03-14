package com.dripify.web.advice;

import com.dripify.exception.UserUpdateException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler({IllegalArgumentException.class, NoResourceFoundException.class})
    public ModelAndView handleNotFoundException(Exception e) {

        return new ModelAndView("500-internal-server-error-page");
    }



}

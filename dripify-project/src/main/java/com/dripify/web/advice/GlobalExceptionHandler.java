package com.dripify.web.advice;

import com.dripify.exception.UserUpdateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserUpdateException.class)
    public String handleUserUpdateException(UserUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(e.getField() + "Error", e.getMessage());

        return "redirect:/settings/profile";
    }

}

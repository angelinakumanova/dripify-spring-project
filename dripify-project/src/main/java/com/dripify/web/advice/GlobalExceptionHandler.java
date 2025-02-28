package com.dripify.web.advice;

import com.dripify.exception.UsernameUpdateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameUpdateException.class)
    public String handleUsernameUpdateException(UsernameUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());

        return "redirect:/profile/settings";
    }
}

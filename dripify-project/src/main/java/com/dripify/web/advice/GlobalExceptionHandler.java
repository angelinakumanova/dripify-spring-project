package com.dripify.web.advice;

import com.dripify.exception.EmailUpdateException;
import com.dripify.exception.PasswordUpdateException;
import com.dripify.exception.UsernameUpdateException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameUpdateException.class)
    public String handleUsernameUpdateException(UsernameUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("usernameError", e.getMessage());

        return "redirect:/profile/settings";
    }

    @ExceptionHandler(EmailUpdateException.class)
    public String handleEmailUpdateException(EmailUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("emailError", e.getMessage());

        return "redirect:/profile/settings";
    }

    @ExceptionHandler(PasswordUpdateException.class)
    public String handlePasswordUpdateException(PasswordUpdateException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("passwordError", e.getMessage());

        return "redirect:/profile/settings";
    }
}

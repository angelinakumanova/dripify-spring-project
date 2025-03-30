package com.dripify.user.listener;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.event.UserDeactivationEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserDeactivationListener {

    @TransactionalEventListener
    public void logoutDeactivatedUser(UserDeactivationEvent event) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof AuthenticationMetadata authenticationMetadata) {
            if (authenticationMetadata.getUsername().equals(event.getUser().getUsername())) {
                SecurityContextHolder.getContext().setAuthentication(null);
            }
        }
    }
}

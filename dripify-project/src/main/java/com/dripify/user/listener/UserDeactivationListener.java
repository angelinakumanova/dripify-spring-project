package com.dripify.user.listener;

import com.dripify.security.AuthenticationMetadata;
import com.dripify.user.event.UserDeactivationEvent;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserDeactivationListener {

    private final SessionRegistry sessionRegistry;

    public UserDeactivationListener(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @TransactionalEventListener
    public void logoutDeactivatedUser(UserDeactivationEvent event) {

        sessionRegistry.getAllPrincipals()
                .stream()
                .filter(p -> p instanceof AuthenticationMetadata)
                .map(p -> (AuthenticationMetadata) p)
                .filter(p -> p.getUserId().equals(event.getUser().getId()))
                .forEach(p -> sessionRegistry.getAllSessions(p, false)
                        .forEach(SessionInformation::expireNow));
    }
}

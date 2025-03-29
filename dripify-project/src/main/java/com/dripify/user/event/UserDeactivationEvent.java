package com.dripify.user.event;

import com.dripify.user.model.User;
import lombok.Getter;

@Getter
public class UserDeactivationEvent {

    private final User user;

    public UserDeactivationEvent(User user) {
        this.user = user;
    }
}

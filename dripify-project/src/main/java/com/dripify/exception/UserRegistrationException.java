package com.dripify.exception;

import lombok.Getter;

@Getter
public class UserRegistrationException extends RuntimeException {
    private final String field;

    public UserRegistrationException(String field,String message) {
        super(message);
        this.field = field;
    }
}

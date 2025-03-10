package com.dripify.exception;

import lombok.Getter;

@Getter
public class UserUpdateException extends RuntimeException {
    private final String field;

    public UserUpdateException(String field, String message) {
        super(message);
        this.field = field;
    }
}

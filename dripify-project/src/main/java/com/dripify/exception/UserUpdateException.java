package com.dripify.exception;

public class UserUpdateException extends RuntimeException {
    private String field;

    public UserUpdateException(String field, String message) {
        super(message);
        this.field = field;
    }

    public String getField() {
        return field;
    }
}

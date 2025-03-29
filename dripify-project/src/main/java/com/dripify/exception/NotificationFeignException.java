package com.dripify.exception;

public class NotificationFeignException extends RuntimeException {
    public NotificationFeignException(String message) {
        super(message);
    }
}

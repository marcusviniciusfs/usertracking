package com.rd;

public class UserTrackingException extends Exception {

    public UserTrackingException(final String message) {
        super(message);
    }

    public UserTrackingException(final String message, final Throwable cause) {
        super(message, cause);
    }
}

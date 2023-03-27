package com.krekerok.blogapp.exception;

public class ActivationCodeNotFoundException extends RuntimeException {

    public ActivationCodeNotFoundException() {
        super();
    }

    public ActivationCodeNotFoundException(String message) {
        super(message);
    }

    public ActivationCodeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivationCodeNotFoundException(Throwable cause) {
        super(cause);
    }
}

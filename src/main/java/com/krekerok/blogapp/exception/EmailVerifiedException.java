package com.krekerok.blogapp.exception;

public class EmailVerifiedException extends RuntimeException {

    public EmailVerifiedException() {
        super();
    }

    public EmailVerifiedException(String message) {
        super(message);
    }

    public EmailVerifiedException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailVerifiedException(Throwable cause) {
        super(cause);
    }
}

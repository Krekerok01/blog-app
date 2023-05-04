package com.krekerok.blogapp.exception;

public class ForbiddingException extends RuntimeException{

    public ForbiddingException() {
        super();
    }

    public ForbiddingException(String message) {
        super(message);
    }

    public ForbiddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddingException(Throwable cause) {
        super(cause);
    }
}

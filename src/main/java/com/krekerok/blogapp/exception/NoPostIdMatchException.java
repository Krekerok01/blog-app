package com.krekerok.blogapp.exception;

public class NoPostIdMatchException extends RuntimeException{

    public NoPostIdMatchException() {
        super();
    }

    public NoPostIdMatchException(String message) {
        super(message);
    }

    public NoPostIdMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPostIdMatchException(Throwable cause) {
        super(cause);
    }
}

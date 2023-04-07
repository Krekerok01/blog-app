package com.krekerok.blogapp.exception;

public class NoBlogIdMatchException extends RuntimeException{

    public NoBlogIdMatchException() {
        super();
    }

    public NoBlogIdMatchException(String message) {
        super(message);
    }

    public NoBlogIdMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBlogIdMatchException(Throwable cause) {
        super(cause);
    }
}

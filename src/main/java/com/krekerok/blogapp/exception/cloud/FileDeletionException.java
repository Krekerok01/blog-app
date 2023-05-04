package com.krekerok.blogapp.exception.cloud;

public class FileDeletionException extends RuntimeException{

    public FileDeletionException() {
        super();
    }

    public FileDeletionException(String message) {
        super(message);
    }

    public FileDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileDeletionException(Throwable cause) {
        super(cause);
    }
}

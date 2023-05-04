package com.krekerok.blogapp.exception;

public class PostCommentNotFoundException extends RuntimeException{

    public PostCommentNotFoundException() {
        super();
    }

    public PostCommentNotFoundException(String message) {
        super(message);
    }

    public PostCommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostCommentNotFoundException(Throwable cause) {
        super(cause);
    }
}

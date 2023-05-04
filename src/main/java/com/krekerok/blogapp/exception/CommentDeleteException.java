package com.krekerok.blogapp.exception;

public class CommentDeleteException extends RuntimeException{

    public CommentDeleteException() {
        super();
    }

    public CommentDeleteException(String message) {
        super(message);
    }

    public CommentDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentDeleteException(Throwable cause) {
        super(cause);
    }
}

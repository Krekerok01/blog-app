package com.krekerok.blogapp.exception;


import com.krekerok.blogapp.dto.responses.ExceptionResponseDto;
import com.krekerok.blogapp.exception.cloud.FileDeletionException;
import com.krekerok.blogapp.exception.cloud.FileUploadException;
import com.krekerok.blogapp.exception.data.PostCommentNotFoundException;
import com.krekerok.blogapp.exception.data.ActivationCodeNotFoundException;
import com.krekerok.blogapp.exception.data.BlogExistsException;
import com.krekerok.blogapp.exception.data.BlogNotFoundException;
import com.krekerok.blogapp.exception.data.FieldExistsException;
import com.krekerok.blogapp.exception.data.PostNotFoundException;
import com.krekerok.blogapp.exception.data.UserNotFoundException;
import com.krekerok.blogapp.exception.sucurity.ForbiddingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler({
        FieldExistsException.class,
        BlogExistsException.class,
        FileUploadException.class,
        FileDeletionException.class})
    public ResponseEntity<ExceptionResponseDto> handleApplicationException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionResponseDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ActivationCodeNotFoundException.class,
        UserNotFoundException.class,
        BlogNotFoundException.class,
        PostNotFoundException.class,
        PostCommentNotFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase()),
            HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(ForbiddingException.class)
    public ResponseEntity<ExceptionResponseDto> handleForbiddingExceptions(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionResponseDto(e.getMessage(), HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase()),
            HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponseDto> parameterExceptionHandler(
        MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                return new ResponseEntity<>(
                    new ExceptionResponseDto(error.getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()),
                    HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(
            new ExceptionResponseDto("Argument validation failed", HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ExceptionResponseDto> handleBindException(BindException ex) {
        if (ex.hasErrors()) {
            for (FieldError error : ex.getFieldErrors()) {
                return new ResponseEntity<>(
                    new ExceptionResponseDto(error.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()),
                    HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(
            new ExceptionResponseDto("Argument validation failed", HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }
}

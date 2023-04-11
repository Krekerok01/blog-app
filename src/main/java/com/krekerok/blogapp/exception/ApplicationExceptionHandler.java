package com.krekerok.blogapp.exception;


import com.krekerok.blogapp.dto.responses.ExceptionResponseDto;
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
        FileDeletionException.class,
        NoBlogIdMatchException.class})
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
        PostNotFoundException.class})
    public ResponseEntity<ExceptionResponseDto> handleNotFoundException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionResponseDto(e.getMessage(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailVerifiedException.class)
    public ResponseEntity<ExceptionResponseDto> handleEmailVerifiedException(EmailVerifiedException e) {
        return new ResponseEntity<>(
            new ExceptionResponseDto(e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()),
            HttpStatus.NOT_ACCEPTABLE);
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

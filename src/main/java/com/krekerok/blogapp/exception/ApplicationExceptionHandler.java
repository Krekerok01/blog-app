package com.krekerok.blogapp.exception;


import com.krekerok.blogapp.dto.responses.ExceptionDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(
        FieldExistsException.class)
    public ResponseEntity<ExceptionDto> handleApplicationException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionDto(e.getMessage(), HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
        ActivationCodeNotFoundException.class,
        UserNotFoundException.class})
    public ResponseEntity<ExceptionDto> handleNotFoundException(RuntimeException e) {
        return new ResponseEntity<>(
            new ExceptionDto(e.getMessage(), HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.getReasonPhrase()),
            HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailVerifiedException.class)
    public ResponseEntity<ExceptionDto> handleEmailVerifiedException(EmailVerifiedException e) {
        return new ResponseEntity<>(
            new ExceptionDto(e.getMessage(), HttpStatus.NOT_ACCEPTABLE.value(),
                HttpStatus.NOT_ACCEPTABLE.getReasonPhrase()),
            HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> parameterExceptionHandler(
        MethodArgumentNotValidException e) {

        BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            for (FieldError error : result.getFieldErrors()) {
                return new ResponseEntity<>(
                    new ExceptionDto(error.getDefaultMessage(), HttpStatus.BAD_REQUEST.value(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()),
                    HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<>(
            new ExceptionDto("Argument validation failed", HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase()),
            HttpStatus.BAD_REQUEST);
    }

}

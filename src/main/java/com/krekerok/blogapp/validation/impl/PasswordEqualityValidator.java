package com.krekerok.blogapp.validation.impl;


import com.krekerok.blogapp.validation.PasswordEquality;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;


public class PasswordEqualityValidator implements ConstraintValidator<PasswordEquality, Object> {

    private String originalField;
    private String confirmField;
    private String message;

    public void initialize(PasswordEquality constraintAnnotation) {
        this.originalField = constraintAnnotation.originalField();
        this.confirmField = constraintAnnotation.confirmField();
        this.message = constraintAnnotation.message();
    }

    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object passwordValue = new BeanWrapperImpl(value).getPropertyValue(originalField);
        Object confirmPassword = new BeanWrapperImpl(value).getPropertyValue(confirmField);

        boolean isValid = passwordValue != null && passwordValue.equals(confirmPassword);
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(message)
                .addPropertyNode(confirmField)
                .addConstraintViolation();
        }
        return isValid;
    }
}

package com.magnoliales.handlebars.annotations.validators;

public interface TypeAnnotationValidator {
    void validate(Class<?> annotatedClass) throws IllegalAnnotationException;
}

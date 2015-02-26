package com.magnoliales.handlebars.annotations.validators;

import java.lang.reflect.Field;

public interface FieldAnnotationValidator {
    void validate(Field field) throws IllegalAnnotationException;
}

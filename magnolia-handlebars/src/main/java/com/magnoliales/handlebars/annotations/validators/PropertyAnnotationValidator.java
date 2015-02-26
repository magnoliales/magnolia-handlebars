package com.magnoliales.handlebars.annotations.validators;

import java.lang.reflect.Field;

public interface PropertyAnnotationValidator {
    void validate(Field field) throws IllegalAnnotationException;
}

package com.magnoliales.handlebars.annotations.validators;

import java.lang.reflect.Method;

public interface MethodAnnotationValidator {
    void validate(Method method) throws IllegalAnnotationException;
}

package com.magnoliales.handlebars.annotations.validators.impl;

import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.annotations.Validator;
import com.magnoliales.handlebars.annotations.validators.IllegalAnnotationException;
import com.magnoliales.handlebars.annotations.validators.TypeAnnotationValidator;

import java.lang.reflect.Modifier;

@Validator(Component.class)
public class ComponentAnnotationValidator implements TypeAnnotationValidator {
    @Override
    public void validate(Class<?> annotatedClass) throws IllegalAnnotationException {
        if (!Modifier.isFinal(annotatedClass.getModifiers())) {
            throw new IllegalAnnotationException("Component class '" + annotatedClass.getName() + "' needs to be final");
        }
    }
}

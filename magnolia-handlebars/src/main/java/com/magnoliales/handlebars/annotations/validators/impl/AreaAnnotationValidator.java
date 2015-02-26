package com.magnoliales.handlebars.annotations.validators.impl;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Validator;
import com.magnoliales.handlebars.annotations.validators.IllegalAnnotationException;
import com.magnoliales.handlebars.annotations.validators.TypeAnnotationValidator;

import java.lang.reflect.Modifier;

@Validator(Area.class)
public class AreaAnnotationValidator implements TypeAnnotationValidator {
    @Override
    public void validate(Class<?> annotatedClass) {
        if (!Modifier.isFinal(annotatedClass.getModifiers())) {
            throw new IllegalAnnotationException("Area class '"
                    + annotatedClass.getName() + "' needs to be final");
        }
        if (annotatedClass.getSuperclass() != Object.class) {
            throw new IllegalAnnotationException("Area class '"
                    + annotatedClass.getName() + "' cannot extend another class");
        }
    }
}

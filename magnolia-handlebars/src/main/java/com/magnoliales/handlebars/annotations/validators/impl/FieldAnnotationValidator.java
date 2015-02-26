package com.magnoliales.handlebars.annotations.validators.impl;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Validator;
import com.magnoliales.handlebars.annotations.validators.IllegalAnnotationException;
import com.magnoliales.handlebars.annotations.validators.PropertyAnnotationValidator;

import java.lang.reflect.Modifier;

@Validator(Field.class)
public class FieldAnnotationValidator implements PropertyAnnotationValidator {
    @Override
    public void validate(java.lang.reflect.Field field) throws IllegalAnnotationException {
        if (!Modifier.isPrivate(field.getModifiers())) {
            throw new IllegalAnnotationException("The field '" + field.getName() + "' of class '"
                    + field.getDeclaringClass().getName() + "' needs to be private");
        }
        // maybe see if there's getter for this one
        // see if there's definition and settings
        //                or factory and settings and optional reader
        //                or it mapped to composite object
        //                or it is mapped to a simple object that we can create editor for
    }
}

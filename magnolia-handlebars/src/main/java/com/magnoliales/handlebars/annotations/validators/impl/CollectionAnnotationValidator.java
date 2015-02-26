package com.magnoliales.handlebars.annotations.validators.impl;

import com.magnoliales.handlebars.annotations.Collection;
import com.magnoliales.handlebars.annotations.Validator;
import com.magnoliales.handlebars.annotations.validators.PropertyAnnotationValidator;
import com.magnoliales.handlebars.annotations.validators.IllegalAnnotationException;

import java.lang.reflect.Field;

@Validator(Collection.class)
public class CollectionAnnotationValidator implements PropertyAnnotationValidator {
    @Override
    public void validate(Field field) throws IllegalAnnotationException {
        // needs to be private
        // not sure about the size
    }
}

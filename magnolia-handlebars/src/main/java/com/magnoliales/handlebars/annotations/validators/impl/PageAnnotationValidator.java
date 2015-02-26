package com.magnoliales.handlebars.annotations.validators.impl;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Validator;
import com.magnoliales.handlebars.annotations.validators.IllegalAnnotationException;
import com.magnoliales.handlebars.annotations.validators.TypeAnnotationValidator;

@Validator(Page.class)
public class PageAnnotationValidator implements TypeAnnotationValidator {
    @Override
    public void validate(Class<?> annotatedClass) throws IllegalAnnotationException {

    }
}

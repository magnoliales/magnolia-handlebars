package com.magnoliales.handlebars.ui.dialogs.processors;

import com.google.inject.Injector;

import java.lang.reflect.Modifier;

public class AreaProcessor extends Processor {

    protected AreaProcessor(Class<?> type, Injector injector) {
        super(type, injector);
    }

    @Override
    protected void validate(Class<?> type) {
        if (!Modifier.isFinal(type.getModifiers())) {
            throw new RuntimeException("The are annotation can only be used on final classes");
        }
        if (!type.getSuperclass().equals(Object.class)) {
            throw new RuntimeException("The are annotated class cannot extend other classes");
        }
    }
}

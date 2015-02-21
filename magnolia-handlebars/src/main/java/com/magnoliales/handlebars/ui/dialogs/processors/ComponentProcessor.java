package com.magnoliales.handlebars.ui.dialogs.processors;

import com.google.inject.Injector;

import java.lang.reflect.Modifier;

public class ComponentProcessor extends Processor {

    protected ComponentProcessor(Class<?> type, Injector injector) {
        super(type, injector);
    }

    @Override
    protected void validate(Class<?> type) {
        if (!Modifier.isFinal(type.getModifiers())) {
            throw new RuntimeException("The component annotation can only be used on final classes");
        }
        if (!type.getSuperclass().equals(Object.class)) {
            throw new RuntimeException("The component annotated class cannot extend other classes");
        }
    }
}

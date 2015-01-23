package com.magnoliales.handlebars.dialogs.processors;

import java.lang.reflect.Modifier;

public class ComponentProcessor extends Processor {

    protected ComponentProcessor(Class<?> type) {
        super(type);
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

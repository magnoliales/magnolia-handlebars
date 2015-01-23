package com.magnoliales.handlebars.dialogs.processors;

import java.lang.reflect.Modifier;

public class AreaProcessor extends Processor {

    protected AreaProcessor(Class<?> type) {
        super(type);
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

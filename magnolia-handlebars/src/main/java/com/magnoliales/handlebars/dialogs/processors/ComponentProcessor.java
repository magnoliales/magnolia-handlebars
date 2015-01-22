package com.magnoliales.handlebars.dialogs.processors;

import java.lang.reflect.Modifier;

public class ComponentProcessor extends AbstractProcessor {
    @Override
    protected void validate(Class<?> type) {
        if (!Modifier.isFinal(type.getModifiers())) {
            throw new RuntimeException("The annotation can only be used on final classes");
        }
        if (!type.getSuperclass().equals(Object.class)) {
            throw new RuntimeException("The annotated class cannot extend other classes");
        }
    }
}

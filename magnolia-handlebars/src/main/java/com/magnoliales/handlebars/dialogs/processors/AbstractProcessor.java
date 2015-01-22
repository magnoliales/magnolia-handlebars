package com.magnoliales.handlebars.dialogs.processors;

import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractProcessor implements Processor {

    private final static Logger logger = LoggerFactory.getLogger(Processor.class);

    public Map<String, ConfiguredTabDefinition> getTabs(Class<?> type) {
        validate(type);
        Map<String, ConfiguredTabDefinition> tabs = new LinkedHashMap<>();

        return tabs;
    }

    public ConfiguredTabDefinition getTab(Class<?> type) {
        // read all the fields and if you find a class flatten the values into that
        return null;
    }

    protected void validate(Class<?> type) {
    }

    protected FieldDefinition getFieldDefinition(String fieldName, Field field) {
        try {
            ConfiguredFieldDefinition definition = field.definition().newInstance();
            definition.setName("fields." + fieldName);
            // @todo add more advanced configuration here
            return definition;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new ProcessorException("Cannot instantiate field definition for " + fieldName, e);
        }
    }

    protected Field getFieldAnnotation(Class<?> type, String fieldName) {
        if (type.equals(Object.class)) {
            throw new ProcessorException("Cannot find the inherited annotation for field " + fieldName);
        }
        for (java.lang.reflect.Field field : type.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                if (field.isAnnotationPresent(Field.class)) {
                    Field annotation = field.getAnnotation(Field.class);
                    if (!annotation.inherits()) {
                        return annotation;
                    }
                }
            }
        }
        return getFieldAnnotation(type.getSuperclass(), fieldName);
    }
}

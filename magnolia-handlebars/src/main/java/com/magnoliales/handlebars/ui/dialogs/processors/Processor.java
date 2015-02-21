package com.magnoliales.handlebars.ui.dialogs.processors;

import com.google.inject.Injector;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Processable;
import com.magnoliales.handlebars.ui.dialogs.transformers.HierarchicalValueTransformer;
import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import info.magnolia.ui.form.definition.ConfiguredFormDefinition;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import net.minidev.json.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private Class<?> type;
    private Injector injector;

    protected Processor(Class<?> type, Injector injector) {
        validate(type);
        this.type = type;
        this.injector = injector;
    }

    public static Processor getInstance(Class<?> type, Injector injector) {
        Processable annotation = getProcessableAnnotation(type);
        if (annotation == null) {
            return null;
        }
        try {
            return annotation.processor()
                    .getDeclaredConstructor(Class.class, Injector.class)
                    .newInstance(type, injector);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate processor", e);
        }
    }

    public ConfiguredFormDefinition process() {
        ConfiguredFormDefinition formDefinition = new ConfiguredFormDefinition();
        Map<String, List<FieldDefinition>> tabs = new LinkedHashMap<>();
        processFields(type, "main", tabs, "");
        for (Map.Entry<String, List<FieldDefinition>> tab : tabs.entrySet()) {
            ConfiguredTabDefinition tabDefinition = new ConfiguredTabDefinition();
            tabDefinition.setName(tab.getKey());
            for (FieldDefinition fieldDefinition : tab.getValue()) {
                tabDefinition.addField(fieldDefinition);
            }
            formDefinition.addTab(tabDefinition);
        }
        return formDefinition;
    }

    protected void processFields(Class<?> type, String scope, Map<String, List<FieldDefinition>> fields,
                                 String fieldNamespace) {

        for (java.lang.reflect.Field field : type.getDeclaredFields()) {
            if (field.isAnnotationPresent(Field.class)) {
                if (!fields.containsKey(scope)) {
                    fields.put(scope, new ArrayList<FieldDefinition>());
                }
                fields.get(scope).add(getFieldDefinition(type, field.getName(), fieldNamespace));
            } else if (!field.getType().isPrimitive()) {
                if (getProcessableAnnotation(field.getType()) == null) {
                    String newScope = scope.equals("main") ? field.getName() : scope;
                    String newFieldNamespace = fieldNamespace + field.getName() + ".";
                    processFields(field.getType(), newScope, fields, newFieldNamespace);
                }
            }
        }
    }

    protected void validate(Class<?> type) {
    }

    protected FieldDefinition getFieldDefinition(Class<?> type, String fieldName, String fieldNamespace) {
        Field field = getFieldAnnotation(type, fieldName);
        ConfiguredFieldDefinition definition;

        Class<? extends ConfiguredFieldDefinition> definitionClass = field.definition();
        Class<? extends FieldDefinitionFactory> factoryClass = field.factory();
        if (definitionClass != ConfiguredFieldDefinition.class
                && factoryClass != FieldDefinitionFactory.class) {
            throw new RuntimeException("Please use either 'factory' or 'definition' for field " + fieldName);
        }
        if (factoryClass != FieldDefinitionFactory.class) {
            FieldDefinitionFactory factory = injector.getInstance(factoryClass);
            injector.injectMembers(factory);
            definition = factory.getInstance();
        } else {
            definition = injector.getInstance(definitionClass);
            injector.injectMembers(definition);
        }

        definition.setName(fieldNamespace + fieldName);
        if (definition.getTransformerClass() != null) {
            throw new RuntimeException("The field " + fieldName + " of class "
                    + type.getName() + " cannot have a transformer");
        }
        definition.setTransformerClass(HierarchicalValueTransformer.class);
        JSONValue.parse(field.settings(), definition);
        return definition;
    }

    protected Field getFieldAnnotation(Class<?> type, String fieldName) {
        if (type.equals(Object.class)) {
            throw new RuntimeException("Cannot find the inherited annotation for field " + fieldName);
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

    protected static Processable getProcessableAnnotation(Class<?> type) {
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Processable.class)) {
                return annotation.annotationType().getAnnotation(Processable.class);
            }
        }
        return null;
    }
}

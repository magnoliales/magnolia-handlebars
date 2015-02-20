package com.magnoliales.handlebars.ui.dialogs.processors;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Processable;
import com.magnoliales.handlebars.ui.dialogs.transformers.HierarchicalValueTransformer;
import info.magnolia.ui.form.definition.ConfiguredFormDefinition;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import net.minidev.json.JSONValue;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class Processor {

    private static final Logger logger = LoggerFactory.getLogger(Processor.class);

    private Class<?> type;

    protected Processor(Class<?> type) {
        validate(type);
        this.type = type;
    }

    public static Processor getInstance(Class<?> type) {
        Processable annotation = getProcessableAnnotation(type);
        if (annotation == null) {
            return null;
        }
        try {
            return annotation.processor().getDeclaredConstructor(Class.class).newInstance(type);
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
        try {
            definition = field.definition().newInstance();

        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Cannot instantiate field definition for " + fieldName, e);
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

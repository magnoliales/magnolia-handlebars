package com.magnoliales.handlebars.ui.dialogs.processors;

import com.google.inject.Injector;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Processable;
import com.magnoliales.handlebars.ui.dialogs.transformers.HierarchicalValueTransformer;
import com.magnoliales.handlebars.ui.dialogs.transformers.TypedMultiValueTransformer;
import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import info.magnolia.ui.form.definition.ConfiguredFormDefinition;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import info.magnolia.ui.form.field.definition.CompositeFieldDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.definition.MultiValueFieldDefinition;
import info.magnolia.ui.form.field.transformer.composite.NoOpCompositeTransformer;
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
                fields.get(scope).add(getFieldDefinition(type, field, fieldNamespace));
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

    protected ConfiguredFieldDefinition getFieldDefinition(Class<?> type,
                                                           java.lang.reflect.Field fieldObject,
                                                           String fieldNamespace) {

        Field field = getFieldAnnotation(type, fieldObject.getName());
        ConfiguredFieldDefinition definition;

        Class<? extends ConfiguredFieldDefinition> definitionClass = field.definition();
        Class<? extends FieldDefinitionFactory> factoryClass = field.factory();
        if (definitionClass != ConfiguredFieldDefinition.class && factoryClass != FieldDefinitionFactory.class) {
            throw new RuntimeException("The field '" + fieldObject.getName() + "' of class '"
                    + type.getName() + "' have both factory and definition set");
        } else if (definitionClass == ConfiguredFieldDefinition.class && factoryClass == FieldDefinitionFactory.class) {
            definition = readFieldDefinition(fieldObject);
        } else if (factoryClass != FieldDefinitionFactory.class) {
            FieldDefinitionFactory factory = injector.getInstance(factoryClass);
            injector.injectMembers(factory);
            definition = factory.getInstance();
            definition.setTransformerClass(HierarchicalValueTransformer.class);
        } else {
            definition = injector.getInstance(definitionClass);
            injector.injectMembers(definition);
            definition.setTransformerClass(HierarchicalValueTransformer.class);
        }
        JSONValue.parse(field.settings(), definition);
        definition.setName(fieldNamespace + fieldObject.getName());

        return definition;
    }

    private ConfiguredFieldDefinition readFieldDefinition(java.lang.reflect.Field field) {
        if (field.getType().isArray()) {
            MultiValueFieldDefinition fieldDefinition = new MultiValueFieldDefinition();
            fieldDefinition.setName(field.getName());
            fieldDefinition.setField(readCompositeFieldDefinition(field.getType().getComponentType(), field.getName()));
            fieldDefinition.setTransformerClass(TypedMultiValueTransformer.class);
            return fieldDefinition;
        } else {
            return readCompositeFieldDefinition(field.getType(), field.getName());
        }
    }

    private CompositeFieldDefinition readCompositeFieldDefinition(Class<?> type, String name) {
        CompositeFieldDefinition compositeFieldDefinition = new CompositeFieldDefinition();
        for (java.lang.reflect.Field field : type.getDeclaredFields()) {
            ConfiguredFieldDefinition itemDefinition = getFieldDefinition(type, field, "");
            itemDefinition.setLabel("");
            itemDefinition.setTransformerClass(null);
            compositeFieldDefinition.addField(itemDefinition);
        }
        compositeFieldDefinition.setName(name);
        compositeFieldDefinition.setTransformerClass(NoOpCompositeTransformer.class);
        return compositeFieldDefinition;
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

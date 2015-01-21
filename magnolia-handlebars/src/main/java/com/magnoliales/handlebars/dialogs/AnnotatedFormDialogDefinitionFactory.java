package com.magnoliales.handlebars.dialogs;

import com.magnoliales.handlebars.annotations.Composite;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Processable;
import com.magnoliales.handlebars.annotations.Value;
import com.magnoliales.handlebars.transformers.NamespacedValueTransformer;
import info.magnolia.ui.admincentral.dialog.action.CancelDialogActionDefinition;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;
import info.magnolia.ui.api.action.ActionDefinition;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.form.definition.ConfiguredFormDefinition;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum AnnotatedFormDialogDefinitionFactory {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedFormDialogDefinitionProvider.class);

    public ConfiguredFormDialogDefinition createFormDialogDefinition(Class<?> elementClass) {

        logger.info("Creating dialog for {}", elementClass);

        boolean processable = false;
        for (Annotation annotation : elementClass.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Processable.class)) {
                processable = true;
            }
        }
        if (!processable) {
            logger.error("Cannot process {} as no processable annotation found", elementClass);
            return null;
        }

        Map<String, ConfiguredTabDefinition> tabs = new LinkedHashMap<>();
        tabs.put("main", new ConfiguredTabDefinition());

        for (Field field : elementClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Value.class)) {
                Value value = getValueAnnotation(elementClass, field.getName());
                tabs.get("main").addField(createFieldDefinition(field.getName(), value, false));
            } else if (field.isAnnotationPresent(Composite.class)) {
                Class<?> compositeClass = field.getType();
                String tab = compositeClass.getSimpleName();
                if (!tabs.containsKey(tab)) {
                    tabs.put(tab, new ConfiguredTabDefinition());
                }
                for (Field compositeField : compositeClass.getDeclaredFields()) {
                    if (compositeField.isAnnotationPresent(Value.class)) {
                        Value value = compositeField.getAnnotation(Value.class);
                        String compositeFieldName = compositeClass.getSimpleName() + compositeField;
                        tabs.get(tab).addField(createFieldDefinition(compositeFieldName, value, true));
                    }
                }
            }
        }

        ConfiguredFormDefinition form = new ConfiguredFormDefinition();
        for (Map.Entry<String, ConfiguredTabDefinition> tab : tabs.entrySet()) {
            tab.getValue().setName(tab.getKey());
            form.addTab(tab.getValue());
        }

        ConfiguredFormDialogDefinition dialog = new ConfiguredFormDialogDefinition();
        dialog.setId("dialogs." + elementClass.getName());
        dialog.setForm(form);
        dialog.setId(elementClass.getName());

        Map<String, ActionDefinition> actions = new HashMap<>();
        actions.put("commit", getCommitAction());
        actions.put("cancel", getCancelAction());
        dialog.setActions(actions);

        return dialog;
    }

    private Value getValueAnnotation(Class<?> nodeClass, String fieldName) {
        if (nodeClass.equals(Object.class)) {
            logger.error("Cannot find the inherited annotation for field {}", fieldName);
            return null;
        }
        for (Field field : nodeClass.getDeclaredFields()) {
            if (field.getName().equals(fieldName)) {
                if (field.isAnnotationPresent(Value.class)) {
                    if (!nodeClass.isAnnotationPresent(Page.class)) {
                        logger.error("Inherited value is found in class {}. The class needs to be annotated as Page.",
                                nodeClass);
                        return null;
                    }
                    Value value = field.getAnnotation(Value.class);
                    if (!value.inherits()) {
                        return value;
                    }
                }
            }
        }
        return getValueAnnotation(nodeClass.getSuperclass(), fieldName);
    }

    private FieldDefinition createFieldDefinition(String fieldName, Value value, boolean namespaced) {
        try {
            ConfiguredFieldDefinition definition = value.fieldDefinition().newInstance();
            definition.setName("fields." + fieldName);
            if (namespaced) {
                definition.setTransformerClass(NamespacedValueTransformer.class);
            }
            return definition;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Cannot instantiate field definition for {}", fieldName);
            return null;
        }
    }

    private ActionDefinition getCommitAction() {
        SaveDialogActionDefinition actionDefinition = new SaveDialogActionDefinition();
        actionDefinition.setName("commit");
        return actionDefinition;
    }

    private ActionDefinition getCancelAction() {
        CancelDialogActionDefinition actionDefinition = new CancelDialogActionDefinition();
        actionDefinition.setName("cancel");
        return actionDefinition;
    }
}

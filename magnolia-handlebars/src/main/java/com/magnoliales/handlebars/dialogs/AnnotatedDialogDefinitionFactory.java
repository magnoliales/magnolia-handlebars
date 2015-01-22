package com.magnoliales.handlebars.dialogs;

import com.magnoliales.handlebars.annotations.Processable;
import com.magnoliales.handlebars.dialogs.processors.Processor;
import info.magnolia.ui.admincentral.dialog.action.CancelDialogActionDefinition;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;
import info.magnolia.ui.api.action.ActionDefinition;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;
import info.magnolia.ui.form.definition.ConfiguredFormDefinition;
import info.magnolia.ui.form.definition.ConfiguredTabDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum AnnotatedDialogDefinitionFactory {

    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedDialogDefinitionProvider.class);

    public List<DialogDefinitionProvider> discoverDialogProviders(Class<?> type) {

        logger.info("Creating dialog for {}", type);

        Processor processor = null;
        for (Annotation annotation : type.getAnnotations()) {
            if (annotation.annotationType().isAnnotationPresent(Processable.class)) {
                try {
                    processor = annotation.annotationType().getAnnotation(Processable.class).processor().newInstance();
                    logger.info("Processor created {}", processor);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error("Cannot instantiate processor");
                    throw new RuntimeException(e);
                }
            }
        }
        if (processor == null) {
            logger.error("Cannot process {} as no processable annotation found", type);
            throw new RuntimeException();
        }

        /*
        Map<String, ConfiguredTabDefinition> tabs = new LinkedHashMap<>();
        tabs.put("main", new ConfiguredTabDefinition());

        for (java.lang.reflect.Field field : elementClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Field.class)) {
                Field value = getValueAnnotation(elementClass, field.getName());
                tabs.get("main").addField(createFieldDefinition(field.getName(), value, elementClass.getName()));
            } else if (field.isAnnotationPresent(Composite.class)) {
                Class<?> compositeClass = field.getType();
                String tab = compositeClass.getSimpleName();
                if (!tabs.containsKey(tab)) {
                    tabs.put(tab, new ConfiguredTabDefinition());
                }
                for (java.lang.reflect.Field compositeField : compositeClass.getDeclaredFields()) {
                    if (compositeField.isAnnotationPresent(Field.class)) {
                        Field value = compositeField.getAnnotation(Field.class);
                        tabs.get(tab).addField(createFieldDefinition(compositeField.getName(), value,
                                elementClass.getName(), compositeClass.getSimpleName()));
                    }
                }
            }
        }
        */

        Map<String, ConfiguredTabDefinition> tabs = processor.getTabs(type);

        ConfiguredFormDefinition form = new ConfiguredFormDefinition();
        for (Map.Entry<String, ConfiguredTabDefinition> tab : tabs.entrySet()) {
            tab.getValue().setName(tab.getKey());
            form.addTab(tab.getValue());
        }

        ConfiguredFormDialogDefinition dialog = new ConfiguredFormDialogDefinition();
        dialog.setId("dialogs." + type.getName());
        dialog.setForm(form);

        Map<String, ActionDefinition> actions = new HashMap<>();
        actions.put("commit", getCommitAction());
        actions.put("cancel", getCancelAction());
        dialog.setActions(actions);

        return null;
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

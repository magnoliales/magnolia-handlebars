package com.magnoliales.handlebars.dialogs;

import com.magnoliales.handlebars.dialogs.processors.Processor;
import info.magnolia.ui.admincentral.dialog.action.CancelDialogActionDefinition;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;
import info.magnolia.ui.api.action.ActionDefinition;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedDialogDefinitionFactoryImpl implements AnnotatedDialogDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedDialogDefinitionProvider.class);

    @Override
    public List<DialogDefinitionProvider> discoverDialogProviders(Class<?> type) {

        List<DialogDefinitionProvider> providers = new ArrayList<>();

        for (Processor processor : getProcessors(type)) {

            ConfiguredFormDialogDefinition dialog = new ConfiguredFormDialogDefinition();
            dialog.setId("dialogs." + type.getName());
            dialog.setForm(processor.process());

            Map<String, ActionDefinition> actions = new HashMap<>();
            actions.put("commit", getCommitAction());
            actions.put("cancel", getCancelAction());
            dialog.setActions(actions);

            providers.add(new AnnotatedDialogDefinitionProvider(dialog));
        }

        return providers;
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

    private List<Processor> getProcessors(Class<?> type) {
        logger.info("Looking for processor for {}", type);
        List<Processor> processors = new ArrayList<>();
        Processor processor = Processor.getInstance(type);
        if (processor != null) {
            processors.add(Processor.getInstance(type));
        }
        for (Field field : type.getDeclaredFields()) {
            if (!field.getType().isPrimitive()) {
                continue;
            }
            Class<?> subType;
            if (field.getType().isArray()) {
                subType = field.getType().getComponentType();
            } else {
                subType = field.getType();
            }
            processors.addAll(getProcessors(subType));
        }
        return processors;
    }
}

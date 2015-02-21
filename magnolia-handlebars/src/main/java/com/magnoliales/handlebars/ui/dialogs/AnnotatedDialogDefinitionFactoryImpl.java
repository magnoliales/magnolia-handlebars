package com.magnoliales.handlebars.ui.dialogs;

import com.google.inject.Injector;
import com.magnoliales.handlebars.ui.dialogs.processors.Processor;
import info.magnolia.ui.admincentral.dialog.action.CancelDialogActionDefinition;
import info.magnolia.ui.admincentral.dialog.action.SaveDialogActionDefinition;
import info.magnolia.ui.api.action.ActionDefinition;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.util.*;

public class AnnotatedDialogDefinitionFactoryImpl implements AnnotatedDialogDefinitionFactory {

    private static final Logger logger = LoggerFactory.getLogger(AnnotatedDialogDefinitionProvider.class);

    private final Injector injector;

    @Inject
    public AnnotatedDialogDefinitionFactoryImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public List<DialogDefinitionProvider> discoverDialogProviders(Class<?> type) {

        List<DialogDefinitionProvider> providers = new ArrayList<>();
        Set<Class<?>> processedTypes = new HashSet<>();

        for (Map.Entry<Class<?>, Processor> entry : getProcessors(type, processedTypes).entrySet()) {

            ConfiguredFormDialogDefinition dialog = new ConfiguredFormDialogDefinition();
            dialog.setId("dialogs." + entry.getKey().getName());
            dialog.setForm(entry.getValue().process());

            Map<String, ActionDefinition> actions = new HashMap<>();
            actions.put("commit", getCommitAction());
            actions.put("cancel", getCancelAction());
            dialog.setActions(actions);

            providers.add(new AnnotatedDialogDefinitionProvider(dialog));

            logger.info("Dialog registered {}", dialog.getId());
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

    private Map<Class<?>, Processor> getProcessors(Class<?> type, Set<Class<?>> processedTypes) {
        Map<Class<?>, Processor> processors = new HashMap<>();
        Processor processor = Processor.getInstance(type, injector);
        if (processor != null) {
            processors.put(type, processor);
        }
        for (Field field : type.getDeclaredFields()) {
            if (field.getType().isPrimitive()) {
                continue;
            }
            Class<?> subType;
            if (field.getType().isArray()) {
                subType = field.getType().getComponentType();
            } else {
                subType = field.getType();
            }
            if (!processedTypes.contains(subType)) {
                processedTypes.add(subType);
                processors.putAll(getProcessors(subType, processedTypes));
            }
        }
        return processors;
    }
}

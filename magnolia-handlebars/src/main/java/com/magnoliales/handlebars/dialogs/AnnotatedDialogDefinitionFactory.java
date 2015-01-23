package com.magnoliales.handlebars.dialogs;

import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;

import java.util.List;

public interface AnnotatedDialogDefinitionFactory {

    List<DialogDefinitionProvider> discoverDialogProviders(Class<?> type);
}

package com.magnoliales.handlebars.ui.dialogs;

import info.magnolia.registry.RegistrationException;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.dialog.definition.FormDialogDefinition;
import info.magnolia.ui.dialog.formdialog.FormDialogPresenter;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;

public class AnnotatedDialogDefinitionProvider implements DialogDefinitionProvider {

    private FormDialogDefinition dialogDefinition;

    public AnnotatedDialogDefinitionProvider(ConfiguredFormDialogDefinition dialogDefinition) {
        this.dialogDefinition = dialogDefinition;
    }

    @Override
    public String getId() {
        return dialogDefinition.getId();
    }

    @Override
    public FormDialogDefinition getDialogDefinition() {
        return dialogDefinition;
    }

    @Override
    public Class<? extends FormDialogPresenter> getPresenterClass() throws RegistrationException {
        return dialogDefinition.getPresenterClass();
    }
}

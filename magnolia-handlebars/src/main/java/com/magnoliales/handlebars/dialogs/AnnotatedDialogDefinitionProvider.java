package com.magnoliales.handlebars.dialogs;

import info.magnolia.registry.RegistrationException;
import info.magnolia.ui.dialog.definition.ConfiguredFormDialogDefinition;
import info.magnolia.ui.dialog.definition.FormDialogDefinition;
import info.magnolia.ui.dialog.formdialog.FormDialogPresenter;
import info.magnolia.ui.dialog.formdialog.FormDialogPresenterImpl;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;

public class AnnotatedDialogDefinitionProvider implements DialogDefinitionProvider {

    private String id;
    private FormDialogDefinition dialogDefinition;

    public AnnotatedDialogDefinitionProvider(String id, ConfiguredFormDialogDefinition dialogDefinition) {
        this.id = id;
        this.dialogDefinition = dialogDefinition;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public FormDialogDefinition getDialogDefinition() {
        return dialogDefinition;
    }

    @Override
    public Class<? extends FormDialogPresenter> getPresenterClass() throws RegistrationException {
        return FormDialogPresenterImpl.class;
    }
}

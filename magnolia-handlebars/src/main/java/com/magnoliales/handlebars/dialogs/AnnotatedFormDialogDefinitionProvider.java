package com.magnoliales.handlebars.dialogs;

import info.magnolia.registry.RegistrationException;
import info.magnolia.ui.dialog.definition.FormDialogDefinition;
import info.magnolia.ui.dialog.formdialog.FormDialogPresenter;
import info.magnolia.ui.dialog.formdialog.FormDialogPresenterImpl;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;

public class AnnotatedFormDialogDefinitionProvider implements DialogDefinitionProvider {

    private final Class<?> nodeClass;

    public AnnotatedFormDialogDefinitionProvider(Class<?> nodeClass) {
        this.nodeClass = nodeClass;
    }

    @Override
    public String getId() {
        return "dialogs." + nodeClass.getName();
    }

    @Override
    public FormDialogDefinition getDialogDefinition() {
        return AnnotatedFormDialogDefinitionFactory.INSTANCE.createFormDialogDefinition(nodeClass);
    }

    @Override
    public Class<? extends FormDialogPresenter> getPresenterClass() throws RegistrationException {
        return FormDialogPresenterImpl.class;
    }
}

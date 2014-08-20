package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextAdapter;
import com.vaadin.data.Item;
import com.vaadin.ui.Field;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.AppController;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.factory.AbstractFieldFactory;

import javax.inject.Inject;

public class TemplatePairSelectorFieldFactory extends AbstractFieldFactory<TemplatePairSelectorFieldDefinition, TemplatePair> {

    private final AppController appController;
    private final UiContext uiContext;
    private final ComponentProvider componentProvider;
    private final ApplicationContextAdapter applicationContextAdapter;

    @Inject
    public TemplatePairSelectorFieldFactory(TemplatePairSelectorFieldDefinition definition, Item relatedFieldItem,
                                            AppController appController, UiContext uiContext,
                                            ComponentProvider componentProvider,
                                            ApplicationContextAdapter applicationContextAdapter) {
        super(definition, relatedFieldItem);
        this.appController = appController;
        this.uiContext = uiContext;
        this.componentProvider = componentProvider;
        this.applicationContextAdapter = applicationContextAdapter;
    }

    @Override
    protected Field<TemplatePair> createFieldComponent() {
        //return new TemplateField(definition, appController, uiContext, componentProvider);
        return new TemplatePairSelectorField(applicationContextAdapter);
    }
}
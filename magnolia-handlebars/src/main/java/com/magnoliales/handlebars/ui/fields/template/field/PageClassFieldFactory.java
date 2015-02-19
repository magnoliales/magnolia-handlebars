package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import com.magnoliales.handlebars.ui.fields.template.PageClass;
import com.vaadin.data.Item;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.factory.AbstractFieldFactory;

import javax.inject.Inject;

public class PageClassFieldFactory<D extends FieldDefinition>
        extends AbstractFieldFactory<PageClassFieldDefinition, PageClass> {

    private HandlebarsRegistry handlebarsRegistry;

    @Inject
    public PageClassFieldFactory(PageClassFieldDefinition definition,
                                 Item relatedFieldItem,
                                 HandlebarsRegistry handlebarsRegistry) {
        super(definition, relatedFieldItem);
        this.handlebarsRegistry = handlebarsRegistry;
    }

    @Override
    protected PageClassField createFieldComponent() {
        return new PageClassField(handlebarsRegistry);
    }

    @Override
    protected Class<?> getFieldType() {
        return PageClassField.class;
    }

    @Override
    protected Class<?> getDefinitionType() {
        return PageClassFieldDefinition.class;
    }
}
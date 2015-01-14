package com.magnoliales.handlebars.fields.template;

import com.magnoliales.handlebars.utils.HandlebarsRegistry;
import com.vaadin.data.Item;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.factory.AbstractFieldFactory;

import javax.inject.Inject;

public class TemplateAndSupplierFieldFactory<D extends FieldDefinition>
        extends AbstractFieldFactory<TemplateAndSupplierFieldDefinition, TemplateAndSupplierPage> {

    private HandlebarsRegistry handlebarsRegistry;

    @Inject
    public TemplateAndSupplierFieldFactory(TemplateAndSupplierFieldDefinition definition,
                                           Item relatedFieldItem,
                                           HandlebarsRegistry handlebarsRegistry) {
        super(definition, relatedFieldItem);
        this.handlebarsRegistry = handlebarsRegistry;
    }

    @Override
    protected TemplateAndSupplierPageField createFieldComponent() {
        return new TemplateAndSupplierPageField(handlebarsRegistry);
    }

    @Override
    protected Class<?> getFieldType() {
        return TemplateAndSupplierPageField.class;
    }

    @Override
    protected Class<?> getDefinitionType() {
        return TemplateAndSupplierFieldDefinition.class;
    }
}
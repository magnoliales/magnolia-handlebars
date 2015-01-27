package com.magnoliales.handlebars.ui.fields.template.validator;

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import info.magnolia.ui.form.validator.factory.AbstractFieldValidatorFactory;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

public class TemplateAndSupplierPageFieldValidatorFactory
        extends AbstractFieldValidatorFactory<TemplateAndSupplierPageFieldValidatorDefinition> {

    private final String pageId;

    public TemplateAndSupplierPageFieldValidatorFactory(Item item,
                                                        TemplateAndSupplierPageFieldValidatorDefinition definition) {
        super(definition);
        pageId = ((JcrNodeAdapter) item).getItemId().getUuid();
    }

    @Override
    public Validator createValidator() {
        return new TemplateAndSupplierPageFieldValidator(definition.getErrorMessage(),
                definition.getHandlebarsRegistry(), pageId);
    }
}

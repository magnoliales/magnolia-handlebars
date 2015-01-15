package com.magnoliales.handlebars.fields.template.validator;

import com.vaadin.data.Validator;
import info.magnolia.ui.form.validator.factory.AbstractFieldValidatorFactory;

public class TemplateAndSupplierPageFieldValidatorFactory
        extends AbstractFieldValidatorFactory<TemplateAndSupplierPageFieldValidatorDefinition> {

    public TemplateAndSupplierPageFieldValidatorFactory(TemplateAndSupplierPageFieldValidatorDefinition definition) {
        super(definition);
    }

    @Override
    public Validator createValidator() {
        return new TemplateAndSupplierPageFieldValidator(definition.getErrorMessage());
    }
}

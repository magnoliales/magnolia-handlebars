package com.magnoliales.handlebars.fields.template.validator;

import info.magnolia.ui.form.validator.definition.ConfiguredFieldValidatorDefinition;

public class TemplateAndSupplierPageFieldValidatorDefinition extends ConfiguredFieldValidatorDefinition {

    public TemplateAndSupplierPageFieldValidatorDefinition() {
        setFactoryClass(TemplateAndSupplierPageFieldValidatorFactory.class);
    }
}

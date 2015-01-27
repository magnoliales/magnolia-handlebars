package com.magnoliales.handlebars.fields.template.validator;

import com.magnoliales.handlebars.utils.HandlebarsRegistry;
import info.magnolia.ui.form.validator.definition.ConfiguredFieldValidatorDefinition;

public class TemplateAndSupplierPageFieldValidatorDefinition extends ConfiguredFieldValidatorDefinition {

    private final HandlebarsRegistry handlebarsRegistry;

    public TemplateAndSupplierPageFieldValidatorDefinition(HandlebarsRegistry handlebarsRegistry) {
        this.handlebarsRegistry = handlebarsRegistry;
        setFactoryClass(TemplateAndSupplierPageFieldValidatorFactory.class);
    }

    public HandlebarsRegistry getHandlebarsRegistry() {
        return handlebarsRegistry;
    }
}

package com.magnoliales.handlebars.ui.fields.template.validator;

import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.ui.form.validator.definition.ConfiguredFieldValidatorDefinition;

public class PageClassFieldValidatorDefinition extends ConfiguredFieldValidatorDefinition {

    private final HandlebarsRegistry handlebarsRegistry;

    public PageClassFieldValidatorDefinition(HandlebarsRegistry handlebarsRegistry) {
        this.handlebarsRegistry = handlebarsRegistry;
        setFactoryClass(PageClassFieldValidatorFactory.class);
    }

    public HandlebarsRegistry getHandlebarsRegistry() {
        return handlebarsRegistry;
    }
}

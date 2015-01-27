package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.ui.fields.template.transformer.TemplateAndSupplierPageFieldTransformer;
import com.magnoliales.handlebars.ui.fields.template.validator.TemplateAndSupplierPageFieldValidatorDefinition;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import javax.inject.Inject;

public class TemplateAndSupplierPageFieldDefinition extends ConfiguredFieldDefinition {

    @Inject
    public TemplateAndSupplierPageFieldDefinition(HandlebarsRegistry handlebarsRegistry) {
        setTransformerClass(TemplateAndSupplierPageFieldTransformer.class);
        setRequired(true);
        addValidator(new TemplateAndSupplierPageFieldValidatorDefinition(handlebarsRegistry));
    }
}

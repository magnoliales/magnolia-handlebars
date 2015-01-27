package com.magnoliales.handlebars.fields.template.field;

import com.magnoliales.handlebars.fields.template.transformer.TemplateAndSupplierPageFieldTransformer;
import com.magnoliales.handlebars.fields.template.validator.TemplateAndSupplierPageFieldValidatorDefinition;
import com.magnoliales.handlebars.utils.HandlebarsRegistry;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import javax.inject.Inject;
import javax.jcr.Item;

public class TemplateAndSupplierPageFieldDefinition extends ConfiguredFieldDefinition {

    @Inject
    public TemplateAndSupplierPageFieldDefinition(HandlebarsRegistry handlebarsRegistry) {
        setTransformerClass(TemplateAndSupplierPageFieldTransformer.class);
        setRequired(true);
        addValidator(new TemplateAndSupplierPageFieldValidatorDefinition(handlebarsRegistry));
    }
}

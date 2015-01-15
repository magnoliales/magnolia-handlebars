package com.magnoliales.handlebars.fields.template.field;

import com.magnoliales.handlebars.fields.template.transformer.TemplateAndSupplierPageFieldTransformer;
import com.magnoliales.handlebars.fields.template.validator.TemplateAndSupplierPageFieldValidatorDefinition;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

public class TemplateAndSupplierPageFieldDefinition extends ConfiguredFieldDefinition {

    public TemplateAndSupplierPageFieldDefinition() {
        setTransformerClass(TemplateAndSupplierPageFieldTransformer.class);
        setRequired(true);
        addValidator(new TemplateAndSupplierPageFieldValidatorDefinition());
    }
}

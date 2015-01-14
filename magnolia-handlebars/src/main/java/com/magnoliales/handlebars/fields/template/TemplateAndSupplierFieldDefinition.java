package com.magnoliales.handlebars.fields.template;

import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

public class TemplateAndSupplierFieldDefinition extends ConfiguredFieldDefinition {

    public TemplateAndSupplierFieldDefinition() {
        setTransformerClass(TemplateAndSupplierFieldTransformer.class);
        setRequired(true);
        // @todo add validators
    }
}

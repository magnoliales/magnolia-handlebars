package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.ui.fields.template.transformer.PageClassFieldTransformer;
import com.magnoliales.handlebars.ui.fields.template.validator.PageClassFieldValidatorDefinition;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;

import javax.inject.Inject;

public class PageClassFieldDefinition extends ConfiguredFieldDefinition {

    @Inject
    public PageClassFieldDefinition(HandlebarsRegistry handlebarsRegistry) {
        setTransformerClass(PageClassFieldTransformer.class);
        setRequired(true);
        addValidator(new PageClassFieldValidatorDefinition(handlebarsRegistry));
    }
}

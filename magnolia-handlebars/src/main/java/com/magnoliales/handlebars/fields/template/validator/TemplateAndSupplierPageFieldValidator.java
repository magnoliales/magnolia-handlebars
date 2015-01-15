package com.magnoliales.handlebars.fields.template.validator;

import com.magnoliales.handlebars.fields.template.TemplateAndSupplierPage;
import com.vaadin.data.validator.AbstractValidator;
import info.magnolia.i18nsystem.SimpleTranslator;

import javax.inject.Inject;

public class TemplateAndSupplierPageFieldValidator extends AbstractValidator<TemplateAndSupplierPage> {

    public TemplateAndSupplierPageFieldValidator(String errorMessage) {
        super(errorMessage);
    }

    @Override
    protected boolean isValidValue(TemplateAndSupplierPage value) {
        return value != null && value.getTemplate() != null && !value.getTemplate().isEmpty();
    }

    @Override
    public Class<TemplateAndSupplierPage> getType() {
        return TemplateAndSupplierPage.class;
    }
}

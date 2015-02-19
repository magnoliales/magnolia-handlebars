package com.magnoliales.handlebars.ui.fields.template.validator;

import com.vaadin.data.Validator;
import info.magnolia.ui.form.validator.factory.AbstractFieldValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageClassFieldValidatorFactory extends AbstractFieldValidatorFactory<PageClassFieldValidatorDefinition> {

    private static final Logger logger = LoggerFactory.getLogger(PageClassFieldValidatorFactory.class);

    public PageClassFieldValidatorFactory(PageClassFieldValidatorDefinition definition) {
        super(definition);
    }

    @Override
    public Validator createValidator() {
        return new PageClassFieldValidator(definition.getErrorMessage(), definition.getHandlebarsRegistry());
    }
}

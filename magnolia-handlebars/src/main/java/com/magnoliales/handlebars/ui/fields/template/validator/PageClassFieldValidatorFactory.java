package com.magnoliales.handlebars.ui.fields.template.validator;

import com.vaadin.data.Item;
import com.vaadin.data.Validator;
import info.magnolia.ui.form.validator.factory.AbstractFieldValidatorFactory;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageClassFieldValidatorFactory extends AbstractFieldValidatorFactory<PageClassFieldValidatorDefinition> {

    private final static Logger logger = LoggerFactory.getLogger(PageClassFieldValidatorFactory.class);

    private final String pageId;

    public PageClassFieldValidatorFactory(Item item, PageClassFieldValidatorDefinition definition) {
        super(definition);
        if (item instanceof JcrNodeAdapter) {
            pageId = ((JcrNodeAdapter) item).getItemId().getUuid();
        } else {
            pageId = null;
            logger.warn("Expecting JcrNodeAdapter instead of {}", item);
        }
    }

    @Override
    public Validator createValidator() {
        return new PageClassFieldValidator(definition.getErrorMessage(), definition.getHandlebarsRegistry(), pageId);
    }
}

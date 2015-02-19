package com.magnoliales.handlebars.ui.fields.template.validator;

import com.magnoliales.handlebars.rendering.definition.HandlebarsPageDefinition;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import com.magnoliales.handlebars.ui.fields.template.PageClass;
import com.vaadin.data.validator.AbstractValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class PageClassFieldValidator extends AbstractValidator<PageClass> {

    public static final Logger logger = LoggerFactory.getLogger(PageClassFieldValidator.class);

    private final HandlebarsRegistry handlebarsRegistry;

    public PageClassFieldValidator(String errorMessage, HandlebarsRegistry handlebarsRegistry) {
        super(errorMessage);
        this.handlebarsRegistry = handlebarsRegistry;
    }

    @Override
    protected boolean isValidValue(PageClass value) {
        if (value == null || value.getPageClassName() == null || value.getPageClassName().isEmpty()) {
            return false;
        }
        HandlebarsPageDefinition pageDefinition = (HandlebarsPageDefinition) handlebarsRegistry.getTemplateDefinition(value.getPageClassName());
        if (pageDefinition == null) {
            return false;
        }
        if (!pageDefinition.hasParent()) {
            return true;
        }
        if (value.getParentNodeId() == null || value.getParentNodeId().isEmpty()) {
            return false;
        }
        Map<String, String> pages = handlebarsRegistry.getPagesByTemplate(pageDefinition.getParent().getId());
        return pages.containsValue(value.getParentNodeId());
    }

    @Override
    public Class<PageClass> getType() {
        return PageClass.class;
    }
}

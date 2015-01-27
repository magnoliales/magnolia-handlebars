package com.magnoliales.handlebars.fields.template.validator;

import com.magnoliales.handlebars.fields.template.TemplateAndSupplierPage;
import com.magnoliales.handlebars.templates.HandlebarsTemplateDefinition;
import com.magnoliales.handlebars.utils.HandlebarsRegistry;
import com.vaadin.data.Item;
import com.vaadin.data.validator.AbstractValidator;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeItemId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

public class TemplateAndSupplierPageFieldValidator extends AbstractValidator<TemplateAndSupplierPage> {

    public static final Logger logger = LoggerFactory.getLogger(TemplateAndSupplierPageFieldValidator.class);

    private final HandlebarsRegistry handlebarsRegistry;
    private final String pageId;

    public TemplateAndSupplierPageFieldValidator(String errorMessage, HandlebarsRegistry handlebarsRegistry, String pageId) {
        super(errorMessage);
        this.handlebarsRegistry = handlebarsRegistry;
        this.pageId = pageId;
    }

    @Override
    protected boolean isValidValue(TemplateAndSupplierPage value) {
        if (value == null || value.getTemplate() == null || value.getTemplate().isEmpty()) {
            return false;
        }
        HandlebarsTemplateDefinition templateDefinition =
                handlebarsRegistry.getTemplateDefinition(value.getTemplate());
        if (templateDefinition == null) {
            return false;
        }
        if (!templateDefinition.hasParent()) {
            return true;
        }
        if (value.getSupplierPage() == null || value.getSupplierPage().isEmpty() || pageId.equals(value.getSupplierPage())) {
            return false;
        }
        Map<String, String> pages = handlebarsRegistry.getPagesByTemplate(templateDefinition.getParent().getId());
        return pages.containsValue(value.getSupplierPage());
    }

    @Override
    public Class<TemplateAndSupplierPage> getType() {
        return TemplateAndSupplierPage.class;
    }
}

package com.magnoliales.handlebars.templating.definition;

import info.magnolia.registry.RegistrationException;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionProvider;

public class HandlebarsTemplateDefinitionProvider implements TemplateDefinitionProvider {

    private HandlebarsTemplateDefinition handlebarsTemplateDefinition;

    public HandlebarsTemplateDefinitionProvider(HandlebarsTemplateDefinition handlebarsTemplateDefinition) {
        this.handlebarsTemplateDefinition = handlebarsTemplateDefinition;
    }

    @Override
    public String getId() {
        return handlebarsTemplateDefinition.getId();
    }

    @Override
    public TemplateDefinition getTemplateDefinition() throws RegistrationException {
        return handlebarsTemplateDefinition;
    }
}

package com.magnoliales.handlebars.rendering.definition;

import info.magnolia.registry.RegistrationException;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionProvider;

public class HandlebarsTemplateDefinitionProvider implements TemplateDefinitionProvider {

    private TemplateDefinition templateDefinition;

    public HandlebarsTemplateDefinitionProvider(TemplateDefinition templateDefinition) {
        this.templateDefinition = templateDefinition;
    }

    @Override
    public String getId() {
        return templateDefinition.getId();
    }

    @Override
    public TemplateDefinition getTemplateDefinition() throws RegistrationException {
        return templateDefinition;
    }
}

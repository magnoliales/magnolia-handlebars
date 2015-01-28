package com.magnoliales.handlebars.templating.elements;

import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.templating.elements.ComponentElement;

public class HandlebarsComponentElement extends ComponentElement {

    public HandlebarsComponentElement(ServerConfiguration server,
                                      RenderingContext renderingContext,
                                      RenderingEngine renderingEngine,
                                      TemplateDefinitionAssignment templateDefinitionAssignment) {
        super(server, renderingContext, renderingEngine, templateDefinitionAssignment);
    }
}

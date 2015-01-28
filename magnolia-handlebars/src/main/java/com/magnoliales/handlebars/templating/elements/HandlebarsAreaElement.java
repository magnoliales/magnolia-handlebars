package com.magnoliales.handlebars.templating.elements;

import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.variation.RenderableVariationResolver;
import info.magnolia.templating.elements.AreaElement;

public class HandlebarsAreaElement extends AreaElement {

    public HandlebarsAreaElement(ServerConfiguration server, RenderingContext renderingContext,
                                 RenderingEngine renderingEngine, RenderableVariationResolver variationResolver) {
        super(server, renderingContext, renderingEngine, variationResolver);
    }
}

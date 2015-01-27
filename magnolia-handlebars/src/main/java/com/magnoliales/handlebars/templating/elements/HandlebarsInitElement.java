package com.magnoliales.handlebars.templating.elements;

import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.templating.elements.InitElement;

public class HandlebarsInitElement extends InitElement {

    public HandlebarsInitElement(ServerConfiguration serverConfiguration, RenderingContext renderingContext) {
        super(serverConfiguration, renderingContext);
    }
}

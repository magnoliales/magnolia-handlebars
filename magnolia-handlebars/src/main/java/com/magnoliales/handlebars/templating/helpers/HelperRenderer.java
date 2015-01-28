package com.magnoliales.handlebars.templating.helpers;

import info.magnolia.rendering.engine.RenderException;
import info.magnolia.templating.elements.TemplatingElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HelperRenderer {

    public static final Logger logger = LoggerFactory.getLogger(HelperRenderer.class);

    protected CharSequence render(TemplatingElement templatingElement) {
        StringBuilder builder = new StringBuilder();
        try {
            templatingElement.begin(builder);
            templatingElement.end(builder);
        } catch (IOException | RenderException e) {
            logger.error("Cannot render templating element {}", templatingElement);
            throw new RuntimeException(e);
        }
        return builder;
    }
}

package com.magnoliales.handlebars.rendering.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.rendering.renderer.HandlebarsRenderer;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.templating.elements.InitElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;

public class HandlebarsInitTemplateHelper implements Helper {

    public static final Logger logger = LoggerFactory.getLogger(HandlebarsInitTemplateHelper.class);

    private final ServerConfiguration serverConfiguration;
    private final RenderingEngine renderingEngine;
    private final HandlebarsRegistry handlebarsRegistry;
    private final NodeObjectMapper mapper;

    @Inject
    public HandlebarsInitTemplateHelper(ServerConfiguration serverConfiguration,
                                        RenderingEngine renderingEngine,
                                        HandlebarsRegistry handlebarsRegistry,
                                        NodeObjectMapper mapper) {
        this.serverConfiguration = serverConfiguration;
        this.renderingEngine = renderingEngine;
        this.handlebarsRegistry = handlebarsRegistry;
        this.mapper = mapper;
    }

    public CharSequence apply(Object context, Options options) throws IOException {
        InitElement element = new InitElement(serverConfiguration, renderingEngine.getRenderingContext());
        StringBuilder builder = new StringBuilder();
        try {
            Node node = options.get("node");

            element.setContent(node);
            element.setNodeIdentifier(node.getIdentifier());
            element.setPath(node.getIdentifier());
            element.setWorkspace(node.getSession().getWorkspace().getName());
            element.setDialog(handlebarsRegistry.getTemplateDefinition(node).getDialog());

            element.begin(builder);
            element.end(builder);
        } catch (RepositoryException | RenderException e) {
            logger.error("Cannot render init element", e);
        }
        return builder;
    }
}

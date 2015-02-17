package com.magnoliales.handlebars.rendering.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.rendering.renderer.HandlebarsRenderer;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.templating.elements.ComponentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;

public class HandlebarsComponentTemplateHelper implements Helper {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsInitTemplateHelper.class);

    private final ServerConfiguration serverConfiguration;
    private final RenderingEngine renderingEngine;
    private final TemplateDefinitionAssignment templateDefinitionAssignment;
    private final HandlebarsRegistry handlebarsRegistry;

    @Inject
    public HandlebarsComponentTemplateHelper(ServerConfiguration serverConfiguration,
                                            RenderingEngine renderingEngine,
                                            TemplateDefinitionAssignment templateDefinitionAssignment,
                                            HandlebarsRegistry handlebarsRegistry) {
        this.serverConfiguration = serverConfiguration;
        this.renderingEngine = renderingEngine;
        this.templateDefinitionAssignment = templateDefinitionAssignment;
        this.handlebarsRegistry = handlebarsRegistry;
    }

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {
        ComponentElement element = new ComponentElement(serverConfiguration, renderingEngine.getRenderingContext(),
                renderingEngine, templateDefinitionAssignment);
        StringBuilder builder = new StringBuilder();
        try {
            Node node = (Node) options.context.get(HandlebarsRenderer.CURRENT_NODE_PROPERTY);
            String name = options.hash("name");

            element.setContent(node);
            element.setNodeIdentifier(node.getIdentifier());
            element.setPath(node.getPath());
            element.setWorkspace(node.getSession().getWorkspace().getName());
            element.setDialog(handlebarsRegistry.getTemplateDefinition(node).getDialog());

            element.begin(builder);
            element.end(builder);
        } catch (RepositoryException | RenderException e) {
            logger.error("Cannot render component", e);
        }
        return builder;
    }
}

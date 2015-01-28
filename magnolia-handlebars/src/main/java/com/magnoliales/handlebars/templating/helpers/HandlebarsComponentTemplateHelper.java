package com.magnoliales.handlebars.templating.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import com.magnoliales.handlebars.templating.elements.HandlebarsComponentElement;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.rendering.template.variation.RenderableVariationResolver;
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
        HandlebarsComponentElement element = new HandlebarsComponentElement(serverConfiguration,
                renderingEngine.getRenderingContext(), renderingEngine, templateDefinitionAssignment);

        Node node = (Node) options.context.get("mgnl:node");
        try {
            element.setContent(node);
            element.setNodeIdentifier(node.getIdentifier());
            element.setPath(node.getPath());
            element.setWorkspace(node.getSession().getWorkspace().getName());
        } catch (RepositoryException e) {
            logger.error("Cannot initialize component element", e);
            throw new RuntimeException(e);
        }

        return new HelperRenderer().render(element);
    }
}

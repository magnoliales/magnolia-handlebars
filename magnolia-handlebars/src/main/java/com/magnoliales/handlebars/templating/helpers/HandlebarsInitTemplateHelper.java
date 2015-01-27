package com.magnoliales.handlebars.templating.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import com.magnoliales.handlebars.templating.elements.HandlebarsInitElement;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.rendering.engine.RenderingEngine;
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

    @Inject
    public HandlebarsInitTemplateHelper(ServerConfiguration serverConfiguration,
                                        RenderingEngine renderingEngine,
                                        HandlebarsRegistry handlebarsRegistry) {
        this.serverConfiguration = serverConfiguration;
        this.renderingEngine = renderingEngine;
        this.handlebarsRegistry = handlebarsRegistry;
    }

    public CharSequence apply(Object context, Options options) throws IOException {
        HandlebarsInitElement element = new HandlebarsInitElement(serverConfiguration,
                renderingEngine.getRenderingContext());
        try {
            Node node = (Node) options.context.get("mgnl:node");

            element.setContent(node);
            element.setNodeIdentifier(node.getIdentifier());
            element.setPath(node.getIdentifier());
            element.setWorkspace(node.getSession().getWorkspace().getName());

            element.setDialog(handlebarsRegistry.getTemplateDefinition(node).getDialog());
        } catch (RepositoryException e) {
            logger.error("Cannot initialize init element", e);
            throw new RuntimeException(e);
        }
        return new HelperRenderer().render(element);
    }
}

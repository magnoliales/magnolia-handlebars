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
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.templating.elements.ComponentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
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
            Node node = options.get("node");
            int index = options.get("@index");

            NodeIterator iterator = node.getNodes();
            Node componentNode = null;
            while (iterator.hasNext()) {
                componentNode = iterator.nextNode();
                String componentClassName = componentNode.getProperty(NodeObjectMapper.CLASS_PROPERTY).getString();
                if (componentClassName.equals(context.getClass().getName())) {
                    if (index == 0) {
                        break;
                    } else {
                        index--;
                    }
                }
            }
            if (componentNode == null) {
                throw new AssertionError("Cannot find component node");
            }

            element.setContent(componentNode);
            element.setNodeIdentifier(componentNode.getIdentifier());
            element.setPath(componentNode.getPath());
            element.setWorkspace(componentNode.getSession().getWorkspace().getName());
            element.setDialog(handlebarsRegistry.getTemplateDefinition(componentNode).getDialog());

            element.begin(builder);
            element.end(builder);
        } catch (RepositoryException | RenderException e) {
            logger.error("Cannot render component", e);
        }
        return builder;
    }
}

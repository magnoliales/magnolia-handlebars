package com.magnoliales.handlebars.templating.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import com.magnoliales.handlebars.templating.definition.HandlebarsAreaDefinition;
import com.magnoliales.handlebars.templating.definition.HandlebarsTemplateDefinition;
import com.magnoliales.handlebars.templating.elements.HandlebarsAreaElement;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.variation.RenderableVariationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;

public class HandlebarsAreaTemplateHelper implements Helper {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsAreaTemplateHelper.class);

    private final ServerConfiguration serverConfiguration;
    private final RenderingEngine renderingEngine;
    private final RenderableVariationResolver renderableVariationResolver;
    private final HandlebarsRegistry handlebarsRegistry;

    @Inject
    public HandlebarsAreaTemplateHelper(ServerConfiguration serverConfiguration,
                                        RenderingEngine renderingEngine,
                                        RenderableVariationResolver renderableVariationResolver,
                                        HandlebarsRegistry handlebarsRegistry) {
        this.serverConfiguration = serverConfiguration;
        this.renderingEngine = renderingEngine;
        this.renderableVariationResolver = renderableVariationResolver;
        this.handlebarsRegistry = handlebarsRegistry;
    }

    public CharSequence apply(Object context, Options options) throws IOException {
        HandlebarsAreaElement element = new HandlebarsAreaElement(serverConfiguration,
                renderingEngine.getRenderingContext(), renderingEngine, renderableVariationResolver);
        try {
            Node node = (Node) options.context.get("mgnl:node");
            String name = options.hash("name");

            HandlebarsTemplateDefinition templateDefinition;
            HandlebarsAreaDefinition areaDefinition = null;
            while (areaDefinition == null) {
                templateDefinition = handlebarsRegistry.getTemplateDefinition(node);
                if (templateDefinition.hasArea(name)) {
                    areaDefinition = templateDefinition.getArea(name);
                } else {
                    String supplierPage = node.getProperty("mgnl:supplierPage").getString();
                    node = node.getSession().getNodeByIdentifier(supplierPage);
                }
            }

            Node areaNode;
            if (node.hasNode(name)) {
                areaNode = node.getNode(name);
            } else {
                areaNode = node.addNode(name, NodeTypes.Area.NAME);
                areaNode.setProperty("mgnl:template", areaDefinition.getAreaType().getName());
                areaNode.getSession().save();
            }

            element.setContent(areaNode);
            element.setNodeIdentifier(areaNode.getIdentifier());
            element.setPath(areaNode.getPath());
            element.setWorkspace(areaNode.getSession().getWorkspace().getName());

            element.setName(name);
            element.setArea(handlebarsRegistry.getTemplateDefinition(node).getArea(name));
            element.setDialog(element.getArea().getDialog());

        } catch (RepositoryException e) {
            logger.error("Cannot initialize area element", e);
            throw new RuntimeException(e);
        }
        return new HelperRenderer().render(element);
    }
}

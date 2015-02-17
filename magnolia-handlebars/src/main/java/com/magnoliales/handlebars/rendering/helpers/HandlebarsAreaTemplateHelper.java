package com.magnoliales.handlebars.rendering.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.rendering.renderer.HandlebarsRenderer;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.variation.RenderableVariationResolver;
import info.magnolia.templating.elements.AreaElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

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
        AreaElement element = new AreaElement(serverConfiguration, renderingEngine.getRenderingContext(),
                renderingEngine, renderableVariationResolver);
        StringBuilder builder = new StringBuilder();
        try {
            Node node = options.get("node");
            String name = options.hash("name");

            TemplateDefinition templateDefinition;
            AreaDefinition areaDefinition = null;
            while (areaDefinition == null) {
                templateDefinition = handlebarsRegistry.getTemplateDefinition(node);
                if (templateDefinition.getAreas().containsKey(name)) {
                    areaDefinition = templateDefinition.getAreas().get(name);
                } else {
                    String supplierPage = node.getProperty(NodeObjectMapper.PARENT_PROPERTY).getString();
                    node = node.getSession().getNodeByIdentifier(supplierPage);
                }
            }
            Node areaNode;
            if (node.hasNode(name)) {
                areaNode = node.getNode(name);
            } else {
                areaNode = node.addNode(name, NodeTypes.Area.NAME);
                areaNode.setProperty(NodeObjectMapper.CLASS_PROPERTY, areaDefinition.getId());
                areaNode.getSession().save();
            }
            element.setContent(areaNode);
            element.setNodeIdentifier(areaNode.getIdentifier());
            element.setPath(areaNode.getPath());
            element.setWorkspace(areaNode.getSession().getWorkspace().getName());
            element.setName(name);
            element.setArea(areaDefinition);
            element.setDialog(areaDefinition.getDialog());

            element.begin(builder);
            element.end(builder);

        } catch (RepositoryException | RenderException e) {
            logger.error("Cannot render area element", e);
        }
        return builder;
    }
}

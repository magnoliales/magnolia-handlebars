package com.magnoliales.handlebars.templating.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import com.magnoliales.handlebars.templating.definition.HandlebarsAreaDefinition;
import com.magnoliales.handlebars.templating.elements.HandlebarsAreaElement;
import com.magnoliales.handlebars.templating.elements.HandlebarsInitElement;
import info.magnolia.cms.beans.config.ServerConfiguration;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.variation.DefaultRenderableVariationResolver;
import info.magnolia.rendering.template.variation.RenderableVariationResolver;
import info.magnolia.templating.elements.TemplatingElement;
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

            HandlebarsAreaDefinition definition = handlebarsRegistry.getTemplateDefinition(node).getArea(name);

            Node areaNode;
            if (node.hasNode(name)) {
                areaNode = node.getNode(name);
            } else {
                // @todo scream at people if there's inherited area, but not area defined in the page class
                // @todo, find a better format to show error messages by using magnolia resources
                areaNode = node.addNode(name, NodeTypes.Area.NAME);
                areaNode.setProperty("mgnl:template", definition.getAreaType().getName());
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

    /*
    public CharSequence apply(Object object, Options options) throws IOException {


        String name = options.hash("name");
        String availableComponents = options.hash("components");
        String dialog = options.hash("dialog");
        String type = options.hash("type");
        String label = options.hash("label");
        String description = options.hash("description");
        Boolean editable = options.hash("editable");
        Map<String, Object> contextAttributes = options.hash("contextAttributes");

        Node objectNode = (Node) options.context.get("mgnl:node");
        Node areaNode;
        String nodeIdentifier;
        String path;
        try {
            if (objectNode.hasNode(name)) {
                areaNode = objectNode.getNode(name);
            } else {
                areaNode = objectNode.addNode(name, NodeTypes.Area.NAME);
            }
            nodeIdentifier = areaNode.getIdentifier();
            path = areaNode.getPath();
        } catch (RepositoryException e) {
            logger.error("Cannot read properties from the node {}", objectNode);
            throw new RuntimeException(e);
        }

        final AreaElement templatingElement = createTemplatingElement();

        templatingElement.setContent(areaNode);
        templatingElement.setWorkspace(RepositoryConstants.WEBSITE);
        templatingElement.setNodeIdentifier(nodeIdentifier);
        templatingElement.setPath(path);
        // templatingElement.setArea(areaState.getAreaDefinition());

        templatingElement.setName(name);
        templatingElement.setAvailableComponents(availableComponents);
        templatingElement.setDialog(dialog);
        templatingElement.setType(type);
        templatingElement.setLabel(label);
        templatingElement.setDescription(description);
        templatingElement.setEditable(editable);
        templatingElement.setContextAttributes(contextAttributes);

        return render(templatingElement);

        return null;
    }
    */

}

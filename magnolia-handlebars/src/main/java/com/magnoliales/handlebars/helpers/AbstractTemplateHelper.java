package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.objectfactory.Components;
import info.magnolia.registry.RegistrationException;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.templating.elements.AbstractContentTemplatingElement;
import info.magnolia.templating.elements.TemplatingElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractTemplateHelper<C extends TemplatingElement> implements Helper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsInitTemplateHelper.class);

    public static final String PATH_ATTRIBUTE = "path";
    public static final String UUID_ATTRIBUTE = "uuid";
    public static final String WORKSPACE_ATTRIBUTE = "workspace";
    public static final String CONTENT_ATTRIBUTE = "content";

    protected C createTemplatingElement() {
        // FIXME use scope instead of fetching the RenderingContext for passing it as an argument
        final RenderingEngine renderingEngine = Components.getComponent(RenderingEngine.class);
        final RenderingContext renderingContext = renderingEngine.getRenderingContext();

        return Components.getComponentProvider().newInstance(getTemplatingElementClass(), renderingContext);
    }

    protected Class<C> getTemplatingElementClass() {
        // TODO does this support more than one level of subclasses?
        return (Class<C>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    protected void initContentElement(Options options, AbstractContentTemplatingElement component)  {
        // @todo The freemarker code ensured that options could be cast to the correct type - here I am just assuming
        ContentMap contentMap = options.hash(CONTENT_ATTRIBUTE);
        Node contentNode = contentMap != null ? contentMap.getJCRNode() : null;
        String workspace = options.hash(WORKSPACE_ATTRIBUTE);
        String nodeIdentifier = options.hash(UUID_ATTRIBUTE);
        String path = options.hash(PATH_ATTRIBUTE);

        component.setContent(contentNode);
        component.setWorkspace(workspace);
        component.setNodeIdentifier(nodeIdentifier);
        component.setPath(path);
    }

    protected CharSequence render(AbstractContentTemplatingElement templatingElement) {
        StringBuffer buffer = new StringBuffer();
        try {
            templatingElement.begin(buffer);
            templatingElement.end(buffer);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("IO Error rendering:", e);
        } catch (RenderException e) {
            e.printStackTrace();
            LOGGER.warn("Render Error rendering:t", e);
        }
        return buffer;
    }

    protected AreaState createAreaState(String name, Node node) {
        String templateId = PropertyUtil.getString(node, "mgnl:template");
        TemplateDefinitionRegistry registry = Components.getComponent(TemplateDefinitionRegistry.class);
        TemplateDefinition templateDefinition;
        try {
            templateDefinition = registry.getTemplateDefinition(templateId);
        } catch (RegistrationException e) {
            LOGGER.error("Cannot fetch template definition from registry", e);
            return null;
        }
        if (templateDefinition.getAreas().containsKey(name)) {
            Node areaNode;
            try {
                Session session = node.getSession();
                areaNode = NodeUtil.createPath(node, name, NodeTypes.Area.NAME);
                session.save();
            } catch (RepositoryException e) {
                LOGGER.error("Cannot create area node", e);
                return null;
            }
            return new AreaState(templateDefinition.getAreas().get(name), areaNode);
        } else {
            String supplierPageId = PropertyUtil.getString(node, "mgnl:supplierPage");
            Node supplier;
            try {
                supplier = node.getSession().getNodeByIdentifier(supplierPageId);
            } catch (RepositoryException e) {
                LOGGER.error("Cannot find supplier page", e);
                return null;
            }
            return createAreaState(name, supplier);
        }

    }

    public static final class AreaState {

        private AreaDefinition areaDefinition;
        private Node node;

        private AreaState(AreaDefinition areaDefinition, Node node) {
            this.areaDefinition = areaDefinition;
            this.node = node;
        }

        public AreaDefinition getAreaDefinition() {
            return areaDefinition;
        }

        public Node getNode() {
            return node;
        }
    }
}

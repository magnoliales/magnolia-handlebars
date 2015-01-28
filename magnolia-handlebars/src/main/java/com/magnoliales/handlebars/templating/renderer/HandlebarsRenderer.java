package com.magnoliales.handlebars.templating.renderer;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.context.FieldValueResolver;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.node2bean.Node2BeanException;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.renderer.AbstractRenderer;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.rendering.util.AppendableWriter;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.framework.message.Node2MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandlebarsRenderer extends AbstractRenderer {

    private static final Logger logger = LoggerFactory.getLogger(HandlebarsRenderer.class);

    private Handlebars handlebars;
    private NodeObjectMapper nodeObjectMapper;
    private HandlebarsRegistry handlebarsRegistry;

    @Inject
    public HandlebarsRenderer(RenderingEngine renderingEngine, NodeObjectMapper nodeObjectMapper,
                              HandlebarsRegistry handlebarsRegistry) {
        super(renderingEngine);
        File templateDirectory = new File("src/main/resources/templates");
        TemplateLoader loader;
        if (templateDirectory.exists()) {
            loader = new CompositeTemplateLoader(
                    new FileTemplateLoader(templateDirectory),
                    new ClassPathTemplateLoader("/templates")
            );
        } else {
            loader = new ClassPathTemplateLoader("/templates");
        }

        handlebars = new Handlebars(loader);
        handlebars.with(new ConcurrentMapTemplateCache());

        try {
            Node node = MgnlContext.getJCRSession(RepositoryConstants.CONFIG)
                    .getNode("/modules/handlebars/renderers/handlebars/helpers");
            Map<String, Object> helpers = Node2MapUtil.node2map(node);
            for (Map.Entry<String, Object> entry : helpers.entrySet()) {
                logger.info("Adding handlebars helper {}: {}", entry.getKey(), entry.getValue());
                handlebars.registerHelper(entry.getKey(), (Helper) entry.getValue());
            }
        } catch (Node2BeanException | RepositoryException e) {
            logger.error("Cannot read helpers information", e);
        }

        this.nodeObjectMapper = nodeObjectMapper;
        this.handlebarsRegistry = handlebarsRegistry;
    }

    @Override
    protected Map<String, Object> newContext() {
        return new HashMap<>();
    }

    @Override
    protected void onRender(Node node, RenderableDefinition renderableDefinition, RenderingContext renderingContext,
                            Map<String, Object> context, String templateScript) throws RenderException {

        if (context.containsKey("components")) {
            @SuppressWarnings("unchecked")
            List<ContentMap> contentMaps = (List<ContentMap>) context.get("components");
            for (ContentMap contentMap : contentMaps) {
                try {
                    Class<?> componentClass = Class.forName((String) contentMap.get("mgnl:template"));
                    Node componentNode = contentMap.getJCRNode();
                    Component component = componentClass.getAnnotation(Component.class);
                    onRender(componentNode, renderableDefinition, renderingContext,
                            new HashMap<String, Object>(), component.templateScript());
                } catch (ClassNotFoundException e) {
                    logger.error("Cannot render templateScript", e);
                }
            }
        }
        final AppendableWriter out;
        try {
            out = renderingContext.getAppendable();
            Context combinedContext = Context.newBuilder(nodeObjectMapper.map(node))
                    .combine("mgnl:node", node)
                    .resolver(JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                    .build();
            try {
                Template template = handlebars.compile(templateScript);
                template.apply(combinedContext, out);
            } finally {
                combinedContext.destroy();
            }
        } catch (IOException e) {
            logger.error("Cannot render templateScript", e);
        }
    }
}

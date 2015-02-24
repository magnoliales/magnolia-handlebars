package com.magnoliales.handlebars.rendering.renderer;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.cache.ConcurrentMapTemplateCache;
import com.github.jknack.handlebars.context.JavaBeanValueResolver;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.CompositeTemplateLoader;
import com.github.jknack.handlebars.io.FileTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import com.google.inject.Injector;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.node2bean.Node2BeanException;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.renderer.AbstractRenderer;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.framework.message.Node2MapUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlebarsRenderer extends AbstractRenderer {

    public static final String CURRENT_NODE_PROPERTY = "handlebars:node";
    private static final Logger logger = LoggerFactory.getLogger(HandlebarsRenderer.class);

    private Handlebars handlebars;
    private Injector injector;

    @Inject
    public HandlebarsRenderer(RenderingEngine renderingEngine, Injector injector) {
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

        this.injector = injector;
    }

    @Override
    protected Map<String, Object> newContext() {
        return new HashMap<>();
    }

    @Override
    protected void onRender(Node node,
                            RenderableDefinition renderableDefinition,
                            RenderingContext renderingContext,
                            Map<String, Object> context,
                            String templateScript) throws RenderException {

        Context combinedContext = null;
        try {
            logger.info("Rendering node '{}' with template '{}'", node.getPath(), templateScript);
            NodeObjectMapper mapper = injector.getInstance(NodeObjectMapper.class);
            combinedContext = Context.newBuilder(mapper.map(node))
                    .combine(CURRENT_NODE_PROPERTY, node)
                    .resolver(JavaBeanValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                    .build();
            Template template = handlebars.compile(templateScript);
            template.apply(combinedContext, renderingContext.getAppendable());
        } catch (IOException | RepositoryException e) {
            throw new RenderException(e);
        } finally {
            if (combinedContext != null) {
                combinedContext.destroy();
            }
        }
    }
}

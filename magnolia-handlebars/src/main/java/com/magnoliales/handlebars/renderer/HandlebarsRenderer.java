package com.magnoliales.handlebars.renderer;

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
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.utils.HandlebarsRegistry;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.renderer.AbstractRenderer;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.rendering.util.AppendableWriter;
import info.magnolia.repository.RepositoryConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HandlebarsRenderer extends AbstractRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsRenderer.class);

    private Handlebars handlebars;
    private HandlebarsRegistry handlebarsRegistry;
    private NodeObjectMapper nodeObjectMapper;

    @Inject
    public HandlebarsRenderer(RenderingEngine renderingEngine, HandlebarsRegistry handlebarsRegistry,
                              NodeObjectMapper nodeObjectMapper) {
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

        // @todo, really not sure why node2bean doesn't work on this one
        try {
            Session session = MgnlContext.getJCRSession(RepositoryConstants.CONFIG);
            Node helpersNode = session.getNode("/modules/handlebars/renderers/handlebars/helpers");
            for (Node helperNode : JcrUtils.getChildNodes(helpersNode)) {
                String helperName = PropertyUtil.getString(helperNode, "name");
                String helperClassName = PropertyUtil.getString(helperNode, "class");
                LOGGER.info("Adding handlebars helper {}: {}", helperName, helperClassName);
                Class<?> helperClass = Class.forName(helperClassName);
                Helper helper = (Helper) helperClass.newInstance();
                handlebars.registerHelper(helperName, helper);
            }
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException | RepositoryException e) {
            LOGGER.error("Cannot read helpers information", e);
        }

        this.handlebarsRegistry = handlebarsRegistry;
        this.nodeObjectMapper = nodeObjectMapper;
    }

    @Override
    protected Map<String, Object> newContext() {
        return new HashMap<>();
    }

    @Override
    protected void onRender(Node node, RenderableDefinition renderableDefinition, RenderingContext renderingContext,
                            Map<String, Object> context, String templateScript) throws RenderException {

        final AppendableWriter out;
        try {
            out = renderingContext.getAppendable();
            Context combinedContext = Context.newBuilder(nodeObjectMapper.map(node))
                    .resolver(JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                    .build();
            try {
                Template template = handlebars.compile(templateScript);
                template.apply(combinedContext, out);
            } finally {
                combinedContext.destroy();
            }
        } catch (IOException e) {
            LOGGER.error("Cannot render template", e);
        }
    }
}

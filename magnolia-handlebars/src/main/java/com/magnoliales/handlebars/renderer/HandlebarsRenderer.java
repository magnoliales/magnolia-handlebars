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
import info.magnolia.cms.core.AggregationState;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.render.RenderContext;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.renderer.AbstractRenderer;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.rendering.util.AppendableWriter;
import info.magnolia.repository.RepositoryConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Session;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HandlebarsRenderer extends AbstractRenderer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsRenderer.class);

    private Handlebars handlebars;

    @Inject
    public HandlebarsRenderer(RenderingEngine renderingEngine) {
        super(renderingEngine);
        TemplateLoader loader = new CompositeTemplateLoader(
                new FileTemplateLoader(new File("src/main/resources/templates")),
                new ClassPathTemplateLoader("/templates")
        );
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
        } catch (Exception e) {
            LOGGER.error("Cannot read helpers information", e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setupContext(Map<String, Object> context, Node content, RenderableDefinition definition,
                                RenderingModel<?> model, Object actionResult) {
        super.setupContext(context, content, definition, model, actionResult);
        context.putAll(RenderContext.get().getModel());
    }

    @Override
    protected Map<String, Object> newContext() {
        return new HashMap<String, Object>();
    }

    @Override
    protected String resolveTemplateScript(Node content, RenderableDefinition definition, RenderingModel<?> model,
                                           String actionResult) {
        return RenderContext.get().getTemplateScript();
    }

    @Override
    protected void onRender(Node content, RenderableDefinition definition, RenderingContext renderingContext,
                            Map<String, Object> context, String templateScript) throws RenderException {

        // @todo add localized node resolve
        // final Locale locale = MgnlContext.getAggregationState().getLocale();

        final AppendableWriter out;
        try {
            out = renderingContext.getAppendable();
            AggregationState aggregationState = (AggregationState) context.get("state");
            Node node = aggregationState.getCurrentContentNode();
            Locale locale = aggregationState.getLocale();
            context.put("content", new ChainedContentMap(node, locale));
            Context combinedContext = Context.newBuilder(context)
                    .resolver(JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                    .build();
            try {
                Template template = handlebars.compile(templateScript);
                template.apply(combinedContext, out);
            } finally {
                combinedContext.destroy();
            }
        } catch (Exception e) {
            LOGGER.error("Cannot render template", e);
        }
    }
}

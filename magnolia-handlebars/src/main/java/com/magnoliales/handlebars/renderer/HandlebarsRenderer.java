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
import info.magnolia.module.blossom.render.RenderContext;
import info.magnolia.rendering.context.RenderingContext;
import info.magnolia.rendering.engine.RenderException;
import info.magnolia.rendering.engine.RenderingEngine;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.rendering.renderer.AbstractRenderer;
import info.magnolia.rendering.template.RenderableDefinition;
import info.magnolia.rendering.util.AppendableWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HandlebarsRenderer extends AbstractRenderer {

    private static final Logger log = LoggerFactory.getLogger(HandlebarsRenderer.class);

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
    }

    public void setHelpers(Map<String, Class<? extends Helper>> helpers) {
        for (String helperName : helpers.keySet()) {
            Class<? extends Helper> helperClass = helpers.get(helperName);
            try {
                Helper helper = helperClass.newInstance();
                handlebars.registerHelper(helperName, helper);
            } catch (Exception e) {
                log.error("Cannot instantiate helpers", e);
            }
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
            Context combinedContext = Context.newBuilder(context)
                    .combine("content", new ChainedContentMap(node, locale.toString()))
                    .combine("state", aggregationState)
                    .resolver(JavaBeanValueResolver.INSTANCE, FieldValueResolver.INSTANCE, MapValueResolver.INSTANCE)
                    .build();
            try {
                Template template = handlebars.compile(templateScript);
                template.apply(combinedContext, out);
            } finally {
                combinedContext.destroy();
            }
        } catch (Exception e) {
            log.error("Cannot render template", e);
        }
    }
}

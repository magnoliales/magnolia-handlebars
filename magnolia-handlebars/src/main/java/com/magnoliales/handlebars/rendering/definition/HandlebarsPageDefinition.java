package com.magnoliales.handlebars.rendering.definition;

import com.magnoliales.handlebars.annotations.Page;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.configured.ConfiguredTemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandlebarsPageDefinition extends ConfiguredTemplateDefinition {

    private final boolean singleton;
    private final HandlebarsPageDefinition parent;

    public HandlebarsPageDefinition(Class<?> pageType,
                                    SimpleTranslator translator,
                                    Map<String, AreaDefinition> areas) {
        this(pageType, translator, areas, null);
    }

    public HandlebarsPageDefinition(Class<?> pageType,
                                    SimpleTranslator translator,
                                    Map<String, AreaDefinition> areas,
                                    HandlebarsPageDefinition parent) {

        super(getTemplateAvailability(pageType));

        String name = pageType.getName();
        Page page = pageType.getAnnotation(Page.class);

        this.singleton = page.singleton();
        this.parent = parent;

        this.setId(name);
        this.setName(translator.translate(name));
        this.setTitle(this.getName());
        this.setDialog("dialogs." + name);
        this.setTemplateScript(page.templateScript());
        this.setAreas(areas);
        this.setRenderType("handlebars");
    }

    public boolean singleton() {
        return singleton;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public HandlebarsPageDefinition getParent() {
        if (parent == null) {
            throw new IllegalStateException("Parent template is not defined");
        }
        return parent;
    }

    public static TemplateAvailability getTemplateAvailability(final Class<?> pageType) {
        return new TemplateAvailability() {
            private boolean root;
            private List<String> templates = new ArrayList<>();
            {
                Page page = pageType.getAnnotation(Page.class);
                for (Class<?> parentType : page.parents()) {
                    if (parentType == Page.Root.class) {
                        root = true;
                    } else {
                        templates.add(parentType.getName());
                    }
                }
            }
            @Override
            public boolean isAvailable(Node content, TemplateDefinition templateDefinition) {
                try {
                    return root && content.getSession().getRootNode().getIdentifier().equals(content.getIdentifier())
                            || templates.contains(templateDefinition.getId());
                } catch (RepositoryException e) {
                    throw new RuntimeException("Cannot initialize template availability", e);
                }
            }
        };
    }
}

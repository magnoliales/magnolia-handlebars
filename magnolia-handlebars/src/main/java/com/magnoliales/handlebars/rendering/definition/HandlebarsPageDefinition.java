package com.magnoliales.handlebars.rendering.definition;

import com.magnoliales.handlebars.annotations.Page;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.configured.ConfiguredTemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;

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

        super(new ConfiguredTemplateAvailability());

        String name = pageType.getName();
        Page page = pageType.getAnnotation(Page.class);

        this.singleton = page.singleton();
        this.parent = parent;

        this.setId(name);
        this.setName(translator.translate(name));
        this.setTitle(name);
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
}

package com.magnoliales.handlebars.templating.definition;

import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.annotations.Page;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;

import java.util.List;
import java.util.Map;

public class HandlebarsTemplateDefinition extends ConfiguredTemplateDefinition
        implements Comparable<HandlebarsTemplateDefinition> {

    private Class<?> templateType;
    private boolean singleton;
    private HandlebarsTemplateDefinition parent;

    /**
     * @todo extends template definition factory that is smart enough to discover the proper definition
     */
    public HandlebarsTemplateDefinition(Class<?> templateType,
                                        TemplateAvailability templateAvailability,
                                        SimpleTranslator translator,
                                        HandlebarsTemplateDefinition parent) {

        super(templateAvailability);
        this.templateType = templateType;
        this.parent = parent;

        setId(templateType.getName());
        String name;

        if (templateType.isAnnotationPresent(Page.class)) {
            Page page = templateType.getAnnotation(Page.class);
            setTemplateScript(page.templateScript());
            singleton = page.singleton();
            name = translator.translate("templates." + templateType.getName());

        } else {
            Component component = templateType.getAnnotation(Component.class);
            setTemplateScript(component.templateScript());

            setId(templateType.getName());
            name = translator.translate("components." + templateType.getName());
        }
        setName(name);

        setTitle(templateType.getName());
        setRenderType("handlebars");
    }

    public Class<?> getTemplateType() {
        return templateType;
    }

    public boolean isSingleton() {
        return singleton;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public HandlebarsTemplateDefinition getParent() {
        if (parent == null) {
            throw new IllegalStateException("parent templateScript is not defined");
        }
        return parent;
    }

    public HandlebarsAreaDefinition getArea(String name) {
        return (HandlebarsAreaDefinition) getAreas().get(name);
    }

    public boolean hasArea(String name) {
        return getAreas().containsKey(name);
    }

    @Override
    public int compareTo(HandlebarsTemplateDefinition definition) {
        return getName().compareTo(definition.getName());
    }
}

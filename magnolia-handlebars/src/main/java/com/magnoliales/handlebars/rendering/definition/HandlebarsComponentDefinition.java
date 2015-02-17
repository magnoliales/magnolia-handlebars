package com.magnoliales.handlebars.rendering.definition;

import com.magnoliales.handlebars.annotations.Component;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.configured.ConfiguredTemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;

public class HandlebarsComponentDefinition extends ConfiguredTemplateDefinition {

    public HandlebarsComponentDefinition(Class<?> componentType, SimpleTranslator translator) {

        super(new ConfiguredTemplateAvailability());

        String name = componentType.getName();
        Component component = componentType.getAnnotation(Component.class);

        this.setId(name);
        this.setName(translator.translate(name));
        this.setTitle(this.getName());
        this.setTemplateScript(component.templateScript());
        this.setDialog("dialogs." + name);
        this.setRenderType("handlebars");
    }
}

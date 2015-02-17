package com.magnoliales.handlebars.rendering.definition;

import com.magnoliales.handlebars.annotations.Area;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.configured.ConfiguredAreaDefinition;
import info.magnolia.rendering.template.configured.ConfiguredComponentAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateAvailability;

public class HandlebarsAreaDefinition extends ConfiguredAreaDefinition {

    public HandlebarsAreaDefinition(Class<?> areaType,
                                    SimpleTranslator translator,
                                    Iterable<Class<?>> components) {

        super(new ConfiguredTemplateAvailability());

        String name = areaType.getName();
        Area area = areaType.getAnnotation(Area.class);

        setId(name);
        setDialog("dialogs." + name);
        setTemplateScript(area.templateScript());
        setName(translator.translate(name));

        for (Class<?> component : components) {
            ConfiguredComponentAvailability componentAvailability = new ConfiguredComponentAvailability();
            componentAvailability.setEnabled(true);
            componentAvailability.setId(component.getName());
            addAvailableComponent(component.getName(), componentAvailability);
        }
    }
}

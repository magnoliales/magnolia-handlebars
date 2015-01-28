package com.magnoliales.handlebars.templating.definition;

import com.magnoliales.handlebars.annotations.Area;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.configured.ConfiguredAreaDefinition;
import info.magnolia.rendering.template.configured.ConfiguredComponentAvailability;

import java.util.List;

public class HandlebarsAreaDefinition extends ConfiguredAreaDefinition implements TemplateDefinition {

    private final Class<?> areaType;

    public HandlebarsAreaDefinition(Class<?> areaType,
                                    TemplateAvailability templateAvailability,
                                    SimpleTranslator translator,
                                    List<Class<?>> components) {

        super(templateAvailability);

        this.areaType = areaType;

        Area area = areaType.getAnnotation(Area.class);

        String id = areaType.getName();
        setId(id);
        setDialog("dialogs." + id);
        setTemplateScript(area.templateScript());
        String name = translator.translate("areas." + id);
        this.setName(name);

        for (Class<?> component : components) {
            ConfiguredComponentAvailability componentAvailability = new ConfiguredComponentAvailability();
            componentAvailability.setEnabled(true);
            componentAvailability.setId(component.getName());
            addAvailableComponent(component.getName(), componentAvailability);
        }
    }

    public Class<?> getAreaType() {
        return areaType;
    }
}

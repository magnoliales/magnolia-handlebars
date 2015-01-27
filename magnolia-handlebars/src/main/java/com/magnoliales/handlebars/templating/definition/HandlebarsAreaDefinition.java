package com.magnoliales.handlebars.templating.definition;

import com.magnoliales.handlebars.annotations.Area;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredAreaDefinition;

public class HandlebarsAreaDefinition extends ConfiguredAreaDefinition {

    private final Class<?> areaType;

    public HandlebarsAreaDefinition(Class<?> areaType,
                                    TemplateAvailability templateAvailability,
                                    SimpleTranslator translator) {
        super(templateAvailability);

        this.areaType = areaType;

        Area area = areaType.getAnnotation(Area.class);

        String id = areaType.getName();
        setId(id);
        setDialog("dialogs." + id);
        setTemplateScript(area.template());
        String name = translator.translate("areas." + id);
        this.setName(name);
    }

    public Class<?> getAreaType() {
        return areaType;
    }
}

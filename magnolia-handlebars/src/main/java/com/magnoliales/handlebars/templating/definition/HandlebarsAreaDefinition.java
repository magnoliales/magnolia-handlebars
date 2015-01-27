package com.magnoliales.handlebars.templating.definition;

import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.rendering.template.TemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredAreaDefinition;

public class HandlebarsAreaDefinition extends ConfiguredAreaDefinition {

    public HandlebarsAreaDefinition(Class<?> areaType,
                                    TemplateAvailability templateAvailability,
                                    SimpleTranslator translator) {
        super(templateAvailability);

        String id = areaType.getName();
        setId(id);
        setDialog("dialogs." + id);
        String name = translator.translate("areas." + id);
        this.setName(name);
    }
}

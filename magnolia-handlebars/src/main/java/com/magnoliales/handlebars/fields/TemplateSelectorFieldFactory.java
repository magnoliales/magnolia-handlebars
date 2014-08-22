package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import info.magnolia.ui.form.field.definition.SelectFieldOptionDefinition;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateSelectorFieldFactory extends SelectFieldFactory<TemplateSelectorDefinition> {

    final private TemplateUtils utils;

    @Inject
    public TemplateSelectorFieldFactory(TemplateSelectorDefinition definition, Item relatedFieldItem,
                                        ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        utils = new TemplateUtils(applicationContextContainer);
    }

    @Override
    public List<SelectFieldOptionDefinition> getSelectFieldOptionDefinition() {
        List<SelectFieldOptionDefinition> selectFieldOptionDefinitions = new ArrayList<SelectFieldOptionDefinition>();
        Map<String, String> templates = utils.getTemplates();
        for (String templateId : templates.keySet()) {
            if (utils.isTemplateAvailable(templateId)) {
                SelectFieldOptionDefinition selectFieldOptionDefinition = new SelectFieldOptionDefinition();
                selectFieldOptionDefinition.setValue(templateId);
                selectFieldOptionDefinition.setLabel(templates.get(templateId));
                selectFieldOptionDefinitions.add(selectFieldOptionDefinition);
            }
        }
        return selectFieldOptionDefinitions;
    }
}

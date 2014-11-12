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

    private final TemplateUtils utils;

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
        for (Map.Entry<String, String> entry : templates.entrySet()) {
            if (utils.isTemplateAvailable(entry.getKey())) {
                SelectFieldOptionDefinition selectFieldOptionDefinition = new SelectFieldOptionDefinition();
                selectFieldOptionDefinition.setValue(entry.getKey());
                selectFieldOptionDefinition.setLabel(entry.getValue());
                selectFieldOptionDefinitions.add(selectFieldOptionDefinition);
            }
        }
        return selectFieldOptionDefinitions;
    }
}

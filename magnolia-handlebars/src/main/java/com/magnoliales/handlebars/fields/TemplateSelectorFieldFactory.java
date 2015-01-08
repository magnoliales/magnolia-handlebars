package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import info.magnolia.rendering.template.assignment.TemplateDefinitionAssignment;
import info.magnolia.ui.form.field.definition.SelectFieldOptionDefinition;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateSelectorFieldFactory extends info.magnolia.pages.app.field.TemplateSelectorFieldFactory {

    private final TemplateUtils utils;

    @Inject
    public TemplateSelectorFieldFactory(TemplateSelectorDefinition definition, Item relatedFieldItem,
                                        TemplateDefinitionAssignment templateDefinitionAssignment,
                                        ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        //super(definition, relatedFieldItem, templateDefinitionAssignment);
        utils = new TemplateUtils(applicationContextContainer);
    }

    @Override
    public List<SelectFieldOptionDefinition> getSelectFieldOptionDefinition() {
        List<SelectFieldOptionDefinition> defaultSelectFieldOptionDefinitions = super.getSelectFieldOptionDefinition();
        List<SelectFieldOptionDefinition> availableSelectFieldOptionDefinitions = new ArrayList<SelectFieldOptionDefinition>();
        for(SelectFieldOptionDefinition selectFieldOptionDefinition : defaultSelectFieldOptionDefinitions) {
            if (utils.isTemplateAvailable(selectFieldOptionDefinition.getValue())) {
                availableSelectFieldOptionDefinitions.add(selectFieldOptionDefinition);
            }
        }
        return availableSelectFieldOptionDefinitions;
    }
}

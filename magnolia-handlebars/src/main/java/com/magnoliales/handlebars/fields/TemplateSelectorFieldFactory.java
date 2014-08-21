package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.form.field.definition.SelectFieldOptionDefinition;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class TemplateSelectorFieldFactory extends SelectFieldFactory<TemplateSelectorDefinition> {

    final private ApplicationContextContainer applicationContextContainer;

    @Inject
    public TemplateSelectorFieldFactory(TemplateSelectorDefinition definition, Item relatedFieldItem,
                                        ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        this.applicationContextContainer = applicationContextContainer;
    }

    @Override
    public List<SelectFieldOptionDefinition> getSelectFieldOptionDefinition() {
        // @todo, check singleton templates and remove them from selection completely or add a validator, not sure

        List<SelectFieldOptionDefinition> selectFieldOptionDefinitions = new ArrayList<SelectFieldOptionDefinition>();
        ApplicationContext context = applicationContextContainer.getContext();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Class<?> beanClass = context.getBean(beanDefinitionName).getClass();
            if (beanClass.isAnnotationPresent(Template.class)) {
                Template template = beanClass.getAnnotation(Template.class);
                SelectFieldOptionDefinition selectFieldOptionDefinition = new SelectFieldOptionDefinition();
                selectFieldOptionDefinition.setValue(template.id());
                selectFieldOptionDefinition.setLabel(template.title());
                selectFieldOptionDefinitions.add(selectFieldOptionDefinition);
            }
        }
        return selectFieldOptionDefinitions;
    }

    @Override
    protected Class<?> getDefaultFieldType() {
        return String.class;
    }
}

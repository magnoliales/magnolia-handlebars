package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.ui.form.FormTab;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;
import info.magnolia.ui.vaadin.form.FormSection;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class SupplierPageSelectorFieldFactory extends SelectFieldFactory<SupplierPageSelectorDefinition> {

    final private Map<String, String> parentTemplates;

    @Inject
    public SupplierPageSelectorFieldFactory(SupplierPageSelectorDefinition definition, Item relatedFieldItem,
                                            ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        this.parentTemplates = getParentTemplates(applicationContextContainer.getContext());
    }

    @Override
    protected Class<?> getDefaultFieldType() {
        return String.class;
    }

    @Override
    protected AbstractSelect createFieldComponent() {
        final AbstractSelect supplierPageComboBox = super.createFieldComponent();
        supplierPageComboBox.setVisible(false);
        FormSection formSection = ((FormTab) this.getParent()).getContainer();
        for (final Component sibling : formSection) {
            if (sibling instanceof ComboBox) { // @todo need better test for the component that I need
                final ComboBox templateComboBox = (ComboBox) sibling;
                templateComboBox.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String templateId = (String) templateComboBox.getValue();
                        boolean requiresSupplierPage = parentTemplates.containsKey(templateId);
                        if (requiresSupplierPage) {
                            supplierPageComboBox.removeAllItems();
                            supplierPageComboBox.setRequired(true);
                            supplierPageComboBox.setVisible(true);
                        } else {
                            supplierPageComboBox.setRequired(false);
                            supplierPageComboBox.setVisible(false);
                        }
                    }
                });
            }
        }
        return supplierPageComboBox;
    }

    private Map<String, String> getParentTemplates(ApplicationContext context) {
        Map<String, String> parentTemplates = new HashMap<String, String>();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Class<?> beanClass = context.getBean(beanDefinitionName).getClass();
            if (beanClass.isAnnotationPresent(Template.class)) {
                Template template = beanClass.getAnnotation(Template.class);
                if (beanClass.isAnnotationPresent(ParentTemplate.class)) {
                    Template parentTemplate = beanClass.getAnnotation(ParentTemplate.class)
                            .value().getAnnotation(Template.class);
                    parentTemplates.put(template.id(), parentTemplate.id());
                }
            }
        }
        return parentTemplates;
    }
}

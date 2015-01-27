package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.ui.fields.template.TemplateAndSupplierPage;
import com.magnoliales.handlebars.templating.definition.HandlebarsTemplateDefinition;
import com.magnoliales.handlebars.setup.HandlebarsRegistry;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TemplateAndSupplierPageField extends CustomField<TemplateAndSupplierPage> {

    private static final Logger logger = LoggerFactory.getLogger(TemplateAndSupplierPageField.class);

    private VerticalLayout component;

    private final ComboBox templateField;
    private final ComboBox supplierPageField;

    public TemplateAndSupplierPageField(final HandlebarsRegistry handlebarsRegistry, final String pageId) {

        component = new VerticalLayout();
        component.setSizeFull();
        component.setSpacing(true);

        templateField = new ComboBox();
        templateField.setImmediate(true);
        templateField.setWidth(100, Unit.PERCENTAGE);
        templateField.setImmediate(true);
        for (HandlebarsTemplateDefinition templateDefinition : handlebarsRegistry.getTemplateDefinitions()) {
            if (templateDefinition.isSingleton() &&
                    handlebarsRegistry.pageWithTemplateExists(templateDefinition.getId())) {
                continue;
            }
            templateField.addItem(templateDefinition.getId());
            templateField.setItemCaption(templateDefinition.getId(), templateDefinition.getName());
        }
        templateField.setTextInputAllowed(false);
        templateField.setCaption(null);
        component.addComponent(templateField);

        supplierPageField = new ComboBox();
        supplierPageField.setImmediate(true);
        supplierPageField.setWidth(100, Unit.PERCENTAGE);
        supplierPageField.setImmediate(true);
        supplierPageField.setCaption(null);
        supplierPageField.setVisible(false);
        component.addComponent(supplierPageField);

        // add validator

        ValueChangeListener templateValueChangeListener = new ValueChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChange(Property.ValueChangeEvent event) {
                String template = (String) templateField.getValue();
                HandlebarsTemplateDefinition parentTemplateDefinition = null;

                if (template != null) {
                    HandlebarsTemplateDefinition templateDefinition =
                            handlebarsRegistry.getTemplateDefinition(template);
                    if (templateDefinition.hasParent()) {
                        parentTemplateDefinition = templateDefinition.getParent();
                    }
                }

                supplierPageField.setValue(null);
                if (parentTemplateDefinition != null) {
                    Map<String, String> pages = handlebarsRegistry.getPagesByTemplate(parentTemplateDefinition.getId());
                    supplierPageField.removeAllItems();
                    for (Map.Entry<String, String> entry : pages.entrySet()) {
                        if (!entry.getValue().equals(pageId)) {
                            supplierPageField.addItem(entry.getValue());
                            supplierPageField.setItemCaption(entry.getValue(), entry.getKey());
                        }
                    }
                    supplierPageField.setVisible(true);
                } else {
                    supplierPageField.setVisible(false);
                }

                String supplierPage = (String) supplierPageField.getValue();
                TemplateAndSupplierPage templateAndSupplierPage = new TemplateAndSupplierPage(template, supplierPage);
                ((Property<TemplateAndSupplierPage>) getPropertyDataSource()).setValue(templateAndSupplierPage);
            }
        };

        ValueChangeListener supplierPageValueChangeListener = new ValueChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChange(Property.ValueChangeEvent event) {
                String template = (String) templateField.getValue();
                String supplierPage = (String) supplierPageField.getValue();
                TemplateAndSupplierPage templateAndSupplierPage = new TemplateAndSupplierPage(template, supplierPage);
                ((Property<TemplateAndSupplierPage>) getPropertyDataSource()).setValue(templateAndSupplierPage);
            }
        };

        templateField.addValueChangeListener(templateValueChangeListener);
        supplierPageField.addValueChangeListener(supplierPageValueChangeListener);
    }

    @Override
    protected Component initContent() {
        return component;
    }

    @Override
    public Class<? extends TemplateAndSupplierPage> getType() {
        return TemplateAndSupplierPage.class;
    }

    @Override
    protected void setInternalValue(TemplateAndSupplierPage templateAndSupplierPage) {
        super.setInternalValue(templateAndSupplierPage);
        templateField.select(templateAndSupplierPage.getTemplate());
        supplierPageField.setValue(templateAndSupplierPage.getSupplierPage());
    }
}

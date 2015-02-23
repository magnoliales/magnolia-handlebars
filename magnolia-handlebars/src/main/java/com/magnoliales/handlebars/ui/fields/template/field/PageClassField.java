package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.rendering.definition.HandlebarsPageDefinition;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import com.magnoliales.handlebars.ui.fields.template.PageClass;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import info.magnolia.rendering.template.TemplateDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import java.util.Map;

public class PageClassField extends CustomField<PageClass> {

    private static final Logger logger = LoggerFactory.getLogger(PageClassField.class);

    private VerticalLayout component;

    private final ComboBox pageClassField;
    private final ComboBox parentNodeField;

    public PageClassField(final HandlebarsRegistry handlebarsRegistry,
                          final Node parentNode,
                          final TemplateDefinition parentTemplate) {

        component = new VerticalLayout();
        component.setSizeFull();
        component.setSpacing(true);

        pageClassField = new ComboBox();
        pageClassField.setImmediate(true);
        pageClassField.setWidth(100, Unit.PERCENTAGE);
        pageClassField.setImmediate(true);

        for (TemplateDefinition templateDefinition : handlebarsRegistry.getTemplateDefinitions()) {
            if (templateDefinition instanceof HandlebarsPageDefinition) {
                HandlebarsPageDefinition pageDefinition = (HandlebarsPageDefinition) templateDefinition;
                if (pageDefinition.singleton() && handlebarsRegistry.pageWithTemplateExists(pageDefinition.getId())) {
                    continue;
                }
                if (pageDefinition.getTemplateAvailability().isAvailable(parentNode, parentTemplate)) {
                    pageClassField.addItem(pageDefinition.getId());
                    pageClassField.setItemCaption(pageDefinition.getId(), pageDefinition.getName());
                }
            }
        }
        pageClassField.setTextInputAllowed(false);
        pageClassField.setCaption(null);
        component.addComponent(pageClassField);

        parentNodeField = new ComboBox();
        parentNodeField.setImmediate(true);
        parentNodeField.setWidth(100, Unit.PERCENTAGE);
        parentNodeField.setImmediate(true);
        parentNodeField.setCaption(null);
        parentNodeField.setVisible(false);
        component.addComponent(parentNodeField);

        ValueChangeListener templateValueChangeListener = new ValueChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChange(Property.ValueChangeEvent event) {
                String pageClassName = (String) pageClassField.getValue();
                HandlebarsPageDefinition parentPageDefinition = null;

                if (pageClassName != null) {
                    HandlebarsPageDefinition pageDefinition = (HandlebarsPageDefinition) handlebarsRegistry.getTemplateDefinition(pageClassName);
                    if (pageDefinition.hasParent()) {
                        parentPageDefinition = pageDefinition.getParent();
                    }
                }

                parentNodeField.setValue(null);
                if (parentPageDefinition != null) {
                    Map<String, String> pages = handlebarsRegistry.getPagesByTemplate(parentPageDefinition.getId());
                    parentNodeField.removeAllItems();
                    for (Map.Entry<String, String> entry : pages.entrySet()) {
                        parentNodeField.addItem(entry.getValue());
                        parentNodeField.setItemCaption(entry.getValue(), entry.getKey());
                    }
                    parentNodeField.setVisible(true);
                } else {
                    parentNodeField.setVisible(false);
                }

                String parentNodeId = (String) parentNodeField.getValue();
                PageClass pageClass = new PageClass(pageClassName, parentNodeId);
                ((Property<PageClass>) getPropertyDataSource()).setValue(pageClass);
            }
        };

        ValueChangeListener supplierPageValueChangeListener = new ValueChangeListener() {
            @Override
            @SuppressWarnings("unchecked")
            public void valueChange(Property.ValueChangeEvent event) {
                String pageClassName = (String) pageClassField.getValue();
                String parentNodeId = (String) parentNodeField.getValue();
                PageClass pageClass = new PageClass(pageClassName, parentNodeId);
                ((Property<PageClass>) getPropertyDataSource()).setValue(pageClass);
            }
        };

        pageClassField.addValueChangeListener(templateValueChangeListener);
        parentNodeField.addValueChangeListener(supplierPageValueChangeListener);
    }

    @Override
    protected Component initContent() {
        return component;
    }

    @Override
    public Class<? extends PageClass> getType() {
        return PageClass.class;
    }

    @Override
    protected void setInternalValue(PageClass pageClass) {
        super.setInternalValue(pageClass);
        pageClassField.select(pageClass.getPageClassName());
        parentNodeField.setValue(pageClass.getParentNodeId());
    }
}

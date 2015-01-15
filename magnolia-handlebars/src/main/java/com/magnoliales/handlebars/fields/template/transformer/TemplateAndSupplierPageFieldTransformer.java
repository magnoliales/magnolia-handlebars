package com.magnoliales.handlebars.fields.template.transformer;

import com.magnoliales.handlebars.fields.template.TemplateAndSupplierPage;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;
import info.magnolia.ui.vaadin.integration.jcr.DefaultProperty;
import org.apache.jackrabbit.ocm.manager.ManagerConstant;

import javax.inject.Inject;

public class TemplateAndSupplierPageFieldTransformer extends BasicTransformer<TemplateAndSupplierPage> {

    @Inject
    public TemplateAndSupplierPageFieldTransformer(Item relatedFormItem, ConfiguredFieldDefinition definition, Class<TemplateAndSupplierPage> type) {
        super(relatedFormItem, definition, type);
    }

    @Override
    public void writeToItem(TemplateAndSupplierPage templateAndSupplierPage) {

        @SuppressWarnings("unchecked")
        Property<String> templateProperty = relatedFormItem.getItemProperty("mgnl:template");
        if (templateProperty == null) {
            templateProperty = new DefaultProperty<>(String.class, templateAndSupplierPage.getTemplate());
            relatedFormItem.addItemProperty("mgnl:template", templateProperty);
        } else {
            templateProperty.setValue(templateAndSupplierPage.getTemplate());
        }

        @SuppressWarnings("unchecked")
        Property<String> ocmClassNameProperty =
                relatedFormItem.getItemProperty(ManagerConstant.DISCRIMINATOR_CLASS_NAME_PROPERTY);
        if (ocmClassNameProperty == null) {
            ocmClassNameProperty = new DefaultProperty<>(String.class, templateAndSupplierPage.getTemplate());
            relatedFormItem.addItemProperty(ManagerConstant.DISCRIMINATOR_CLASS_NAME_PROPERTY, ocmClassNameProperty);
        } else {
            ocmClassNameProperty.setValue(templateAndSupplierPage.getTemplate());
        }

        @SuppressWarnings("unchecked")
        Property<String> supplierPageProperty = relatedFormItem.getItemProperty("mgnl:supplierPage");
        if (supplierPageProperty == null) {
            supplierPageProperty = new DefaultProperty<>(String.class, templateAndSupplierPage.getSupplierPage());
            relatedFormItem.addItemProperty("mgnl:supplierPage", supplierPageProperty);
        } else {
            supplierPageProperty.setValue(templateAndSupplierPage.getSupplierPage());
        }
    }

    @Override
    public TemplateAndSupplierPage readFromItem() {

        @SuppressWarnings("unchecked")
        Property<String> templateProperty = relatedFormItem.getItemProperty("mgnl:template");
        @SuppressWarnings("unchecked")
        Property<String> supplierPageProperty = relatedFormItem.getItemProperty("mgnl:supplierPage");

        String templateId = templateProperty == null ? null : templateProperty.getValue();
        String supplierPageId = supplierPageProperty == null ? null : supplierPageProperty.getValue();

        return new TemplateAndSupplierPage(templateId, supplierPageId);
    }
}

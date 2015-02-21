package com.magnoliales.handlebars.ui.fields.template.transformer;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.ui.fields.template.PageClass;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;
import info.magnolia.ui.vaadin.integration.jcr.DefaultProperty;

import javax.inject.Inject;

public class PageClassFieldTransformer extends BasicTransformer<PageClass> {

    @Inject
    public PageClassFieldTransformer(Item relatedFormItem, ConfiguredFieldDefinition definition, Class<PageClass> type) {
        super(relatedFormItem, definition, type);
    }

    @Override
    public void writeToItem(PageClass pageClass) {

        @SuppressWarnings("unchecked")
        Property<String> classProperty = relatedFormItem.getItemProperty(NodeObjectMapper.CLASS_PROPERTY);
        if (classProperty == null) {
            classProperty = new DefaultProperty<>(String.class, pageClass.getPageClassName());
            relatedFormItem.addItemProperty(NodeObjectMapper.CLASS_PROPERTY, classProperty);
        } else {
            classProperty.setValue(pageClass.getPageClassName());
        }

        @SuppressWarnings("unchecked")
        Property<String> parentProperty = relatedFormItem.getItemProperty(NodeObjectMapper.PARENT_PROPERTY);
        if (parentProperty == null) {
            parentProperty = new DefaultProperty<>(String.class, pageClass.getParentNodeId());
            relatedFormItem.addItemProperty(NodeObjectMapper.PARENT_PROPERTY, parentProperty);
        } else {
            parentProperty.setValue(pageClass.getParentNodeId());
        }
    }

    @Override
    public PageClass readFromItem() {

        @SuppressWarnings("unchecked")
        Property<String> classProperty = relatedFormItem.getItemProperty(NodeObjectMapper.CLASS_PROPERTY);
        @SuppressWarnings("unchecked")
        Property<String> parentProperty = relatedFormItem.getItemProperty(NodeObjectMapper.PARENT_PROPERTY);

        String pageClassName = classProperty == null ? null : classProperty.getValue();
        String parentPageId = parentProperty == null ? null : parentProperty.getValue();

        return new PageClass(pageClassName, parentPageId);
    }
}

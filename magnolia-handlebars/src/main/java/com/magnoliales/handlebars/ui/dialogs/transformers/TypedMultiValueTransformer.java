package com.magnoliales.handlebars.ui.dialogs.transformers;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.multi.MultiValueSubChildrenNodePropertiesTransformer;
import info.magnolia.ui.vaadin.integration.jcr.DefaultProperty;
import info.magnolia.ui.vaadin.integration.jcr.JcrNewNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.lang.reflect.Field;

public class TypedMultiValueTransformer extends MultiValueSubChildrenNodePropertiesTransformer {

    public TypedMultiValueTransformer(Item relatedFormItem,
                                      ConfiguredFieldDefinition definition,
                                      Class<PropertysetItem> type) {

        super(relatedFormItem, definition, type);
    }

    @Override
    protected JcrNodeAdapter initializeChildItem(JcrNodeAdapter rootItem, Node rootNode, String childName)
            throws RepositoryException {

        JcrNodeAdapter childItem = super.initializeChildItem(rootItem, rootNode, childName);
        if (childItem instanceof JcrNewNodeAdapter) {
            try {
                Node node = ((JcrNodeAdapter) relatedFormItem).getJcrItem();
                String className = node.getProperty(NodeObjectMapper.CLASS_PROPERTY).getString();
                Class<?> type = Class.forName(className);
                Field field = type.getDeclaredField(definition.getName());
                String itemClassName = field.getType().getComponentType().getName();
                DefaultProperty<String> property = new DefaultProperty<>(String.class, itemClassName);
                childItem.addItemProperty(NodeObjectMapper.CLASS_PROPERTY, property);
            } catch (ClassNotFoundException | NoSuchFieldException e) {
                throw new RuntimeException("Cannot initialize child item", e);
            }
        }
        return childItem;
    }

}

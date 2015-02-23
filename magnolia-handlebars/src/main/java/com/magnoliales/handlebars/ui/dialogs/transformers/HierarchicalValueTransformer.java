package com.magnoliales.handlebars.ui.dialogs.transformers;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;
import info.magnolia.ui.vaadin.integration.jcr.DefaultProperty;
import info.magnolia.ui.vaadin.integration.jcr.JcrNewNodeAdapter;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class HierarchicalValueTransformer extends BasicTransformer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(HierarchicalValueTransformer.class);

    private final Item item;
    private final String propertyName;
    private final Class<?> itemClass;

    public HierarchicalValueTransformer(Item relatedFormItem,
                                        ConfiguredFieldDefinition definition,
                                        Class<Object> type) {
        super(relatedFormItem, definition, type);

        String[] path = definition.getName().split("\\.");
        this.propertyName = path[path.length - 1];
        try {
            Property itemClassProperty = relatedFormItem.getItemProperty(NodeObjectMapper.CLASS_PROPERTY);
            this.itemClass = Class.forName(itemClassProperty.toString());
            this.item = getItem((JcrNodeAdapter) relatedFormItem, path);
        } catch (RepositoryException | ClassNotFoundException | NoSuchFieldException e) {
            logger.error("Cannot initialize transformer for property {} of node {}",
                    definition.getName(), relatedFormItem);
            throw new RuntimeException(e);
        }
    }

    @Override
    protected <T> Property<T> getOrCreateProperty(Class<T> type) {
        @SuppressWarnings("unchecked")
        Property<T> property = item.getItemProperty(propertyName);
        if (property == null) {
            property = new DefaultProperty<T>(type, null);
            item.addItemProperty(propertyName, property);
        }
        return property;
    }

    private Item getItem(JcrNodeAdapter item, String[] path) throws RepositoryException, NoSuchFieldException {
        Class<?> currentClass = itemClass;
        for (int i = 0; i < path.length - 1; i++) {
            currentClass = currentClass.getDeclaredField(path[i]).getType();
            JcrNodeAdapter childItem = (JcrNodeAdapter) item.getChild(path[i]);
            if (childItem == null) {
                Node node = item.getJcrItem();
                Node childNode = null;
                if (node.hasNode(path[i])) {
                    childNode = node.getNode(path[i]);
                }
                if (childNode != null) {
                    childItem = new JcrNodeAdapter(childNode);
                } else {
                    childItem = new JcrNewNodeAdapter(item.getJcrItem(), NodeTypes.ContentNode.NAME, path[i]);
                    DefaultProperty<String> property = new DefaultProperty<>(String.class, currentClass.getName());
                    childItem.addItemProperty(NodeObjectMapper.CLASS_PROPERTY, property);
                }
            }
            item.addChild(childItem);
            childItem.setParent(item);
            item = childItem;
        }
        return item;
    }
}

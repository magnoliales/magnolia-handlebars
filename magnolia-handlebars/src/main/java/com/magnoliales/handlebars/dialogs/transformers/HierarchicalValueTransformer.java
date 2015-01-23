package com.magnoliales.handlebars.dialogs.transformers;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HierarchicalValueTransformer extends BasicTransformer<Object> {

    private static final Logger logger = LoggerFactory.getLogger(HierarchicalValueTransformer.class);

    public HierarchicalValueTransformer(Item relatedFormItem,
                                        ConfiguredFieldDefinition definition,
                                        Class<Object> type) {
        super(relatedFormItem, definition, type);
    }

    @Override
    public void writeToItem(Object newValue) {
        String[] path = basePropertyName.split("\\.");
        super.writeToItem(newValue);
        Node node = ((JcrNodeAdapter) relatedFormItem).getJcrItem();
        int i = 0;
        try {
            while (i < path.length - 1) {
                node = node.addNode(path[i]);
                i++;
            }
            if (newValue instanceof String) {
                node.setProperty(path[i], (String) newValue);
            } else if (newValue instanceof Long) {
                node.setProperty(path[i], (Long) newValue);
            } else if (newValue instanceof Boolean) {
                node.setProperty(path[i], (Boolean) newValue);
            } else if (newValue instanceof BigDecimal) {
                node.setProperty(path[i], (BigDecimal) newValue);
            } else if (newValue instanceof Calendar) {
                node.setProperty(path[i], (Calendar) newValue);
            } else if (newValue instanceof Double) {
                node.setProperty(path[i], (Double) newValue);
            } else {
                node.setProperty(path[i], newValue.toString());
            }
            Property<List> property = getOrCreateProperty(List.class);
        } catch (RepositoryException e) {
            logger.warn("Cannot write property {} to {}", basePropertyName, node);
        }
    }

    @Override
    public Object readFromItem() {
        String[] path = basePropertyName.split("\\.");
        Node node = ((JcrNodeAdapter) relatedFormItem).getJcrItem();
        int i = 0;
        try {
            while (i < path.length - 1) {
                node = node.getNode(path[i]);
                i++;
            }
            Value value = node.getProperty(path[i]).getValue();
            switch (value.getType()) {
                case PropertyType.STRING:
                    return value.getString();
                case PropertyType.LONG:
                    return value.getLong();
                case PropertyType.BOOLEAN:
                    return value.getBoolean();
                case PropertyType.DATE:
                    return value.getDate();
                case PropertyType.DECIMAL:
                    return value.getDecimal();
                case PropertyType.DOUBLE:
                    return value.getDouble();
                default:
                    return value.toString();

            }
        } catch (RepositoryException e) {
            logger.warn("Cannot read property {} from {}", basePropertyName, node);
        }
        return definition.getDefaultValue();
    }
}

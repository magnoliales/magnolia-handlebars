package com.magnoliales.handlebars.mapper;

import com.github.jknack.handlebars.internal.js.RhinoHandlebars;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.jcr.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.lang.reflect.InvocationTargetException;

public class NodeObjectMapperImpl implements NodeObjectMapper {

    public static final Logger logger = LoggerFactory.getLogger(NodeObjectMapperImpl.class);

    @Override
    public Object map(Node node) {
        try {
            String nodeClassName = node.getProperty("mgnl:template").getString();
            Class<?> type = Class.forName(nodeClassName);
            Object object = type.newInstance();
            return map(type, object, node);
        } catch (ClassNotFoundException | RepositoryException | InstantiationException | IllegalAccessException e) {
            logger.error("Cannot map node {}", node);
            return null;
        }
    }

    private Object map(Class<?> type, Object object, Node node)
            throws IllegalAccessException, InstantiationException, RepositoryException {

        while (!type.equals(Object.class)) {
            for (java.lang.reflect.Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (field.isAnnotationPresent(Field.class) && node.hasProperty(fieldName)) {
                    Property property = node.getProperty(fieldName);
                    switch (property.getType()) {
                        case PropertyType.STRING:
                            field.set(object, property.getString());
                            break;
                        default:
                            throw new AssertionError("Not implemented");
                    }
                } else if (node.hasNode(fieldName)) {
                    Object property = field.getType().newInstance();
                    field.set(object, map(field.getType(), property, node.getNode(fieldName)));
                }
            }
            type = type.getSuperclass();
            if (type.isAnnotationPresent(Page.class)) {
                String supplierId = PropertyUtil.getString(node, "mgnl:supplierPage");
                node = node.getSession().getNodeByIdentifier(supplierId);
            }
            map(type, object, node);
        }
        return object;
    }
}

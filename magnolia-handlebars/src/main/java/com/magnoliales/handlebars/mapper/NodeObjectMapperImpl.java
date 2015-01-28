package com.magnoliales.handlebars.mapper;

import com.github.jknack.handlebars.internal.js.RhinoHandlebars;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.jcr.util.NodeUtil;
import info.magnolia.jcr.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
            throws IllegalAccessException, InstantiationException, RepositoryException, ClassNotFoundException {

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
                } else if (field.getType().isArray()) {
                    Class<?> componentType = field.getType().getComponentType();
                    List<Object> items = new ArrayList<>();
                    for (Node childNode : NodeUtil.getNodes(node)) {
                        Class<?> childNodeClass = Class.forName(PropertyUtil.getString(childNode, "mgnl:template"));
                        if (componentType.isAssignableFrom(childNodeClass)) {
                            items.add(map(childNodeClass, childNodeClass.newInstance(), childNode));
                        }
                    }
                    if (items.size() > 0) {
                        field.set(object, toArray(items, componentType));
                    }
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

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(List<?> list, Class<T> type) {
        T[] array = (T[]) java.lang.reflect.Array.newInstance(type, list.size());
        int i = 0;
        for (Object item : list) {
            array[i++] = type.cast(item);
        }
        return array;
    }
}

package com.magnoliales.handlebars.mapper;

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
        String nodeClassName = PropertyUtil.getString(node, "mgnl:template");
        try {
            return map(Class.forName(nodeClassName), node);
        } catch (ClassNotFoundException | RepositoryException | InstantiationException | IllegalAccessException e) {
            logger.error("Cannot map node {} to class '{}'", node, nodeClassName);
            return null;
        }
    }

    private Object map(Class<?> type, Node node)
            throws IllegalAccessException, InstantiationException, RepositoryException {

        Object object = type.newInstance();
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
                field.set(object, map(field.getType(), node.getNode(fieldName)));
            }
        }
        return object;
    }
}

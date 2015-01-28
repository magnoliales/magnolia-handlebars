package com.magnoliales.handlebars.mapper;

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

public class NodeObjectMapperImpl implements NodeObjectMapper {

    private static final Logger logger = LoggerFactory.getLogger(NodeObjectMapperImpl.class);

    @Override
    public Object map(Node node) {
        String objectClassName;
        try {
            objectClassName = node.getProperty(TEMPLATE_PROPERTY).getString();
        } catch (RepositoryException e) {
            logger.error("Cannot read property {} from node {}", TEMPLATE_PROPERTY, node);
            return null;
        }
        Class<?> objectClass;
        Object object;
        try {
            objectClass = Class.forName(objectClassName);
            object = objectClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error("Cannot instantiate object of class '{}'", objectClassName);
            return null;
        }
        while (!objectClass.equals(Object.class)) {
            for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Field.class)) {
                    try {
                        setValue(object, field, node.getProperty(field.getName()));
                    } catch (RepositoryException | IllegalAccessException | InvocationTargetException e) {
                        logger.error("Cannot map property '{}' to bean '{}'", field.getName(), object);
                    }
                }
            }
            objectClass = objectClass.getSuperclass();
            if (objectClass.isAnnotationPresent(Page.class)) {
                try {
                    String supplierId = node.getProperty(SUPPLIER_PAGE_PROPERTY).getString();
                    node = node.getSession().getNodeByIdentifier(supplierId);
                } catch (RepositoryException e) {
                    logger.error("Cannot navigate to the supplier page");
                }
            }
        }
        return object;
    }

    private void setValue(Object bean, java.lang.reflect.Field field, Property property)
            throws RepositoryException, InvocationTargetException, IllegalAccessException {
        field.setAccessible(true);
        switch (property.getType()) {
            case PropertyType.STRING:
                field.set(bean, property.getString());
                break;
        }
    }
}

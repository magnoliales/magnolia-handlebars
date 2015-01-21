package com.magnoliales.handlebars.mapper;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;
import info.magnolia.jcr.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.PropertyType;
import javax.jcr.RepositoryException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class NodeObjectMapperImpl implements NodeObjectMapper {

    public static final Logger logger = LoggerFactory.getLogger(NodeObjectMapperImpl.class);

    @Override
    public Object map(Node node) {
        String nodeClassName = PropertyUtil.getString(node, "mgnl:template");
        Class<?> nodeClass;
        Object bean;
        try {
            nodeClass = Class.forName(nodeClassName);
            bean = nodeClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            logger.error("Cannot instantiate object of class '{}'", nodeClassName);
            return null;
        }
        while (!nodeClass.equals(Object.class)) {
            for (Field field : nodeClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Value.class)) {
                    try {
                        setValue(bean, field, node.getProperty(field.getName()));
                    } catch (RepositoryException | IllegalAccessException | InvocationTargetException e) {
                        logger.error("Cannot map property '{}' to bean '{}'", field.getName(), bean);
                    }
                }
            }
            nodeClass = nodeClass.getSuperclass();
            if (nodeClass.isAnnotationPresent(Page.class)) {
                String supplierId = PropertyUtil.getString(node, "mgnl:supplierPage");
                try {
                    node = node.getSession().getNodeByIdentifier(supplierId);
                } catch (RepositoryException e) {
                    logger.error("Cannot navigate to the supplier page");
                }
            }
        }
        return bean;
    }

    private void setValue(Object bean, Field field, Property property)
            throws RepositoryException, InvocationTargetException, IllegalAccessException {
        field.setAccessible(true);
        switch (property.getType()) {
            case PropertyType.STRING:
                field.set(bean, property.getString());
                break;
        }
    }
}

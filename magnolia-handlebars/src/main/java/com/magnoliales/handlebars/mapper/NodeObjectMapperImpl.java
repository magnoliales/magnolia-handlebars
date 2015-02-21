package com.magnoliales.handlebars.mapper;

import com.google.inject.Injector;
import com.magnoliales.handlebars.annotations.Collection;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.utils.PropertyReader;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.repository.RepositoryConstants;
import org.apache.jackrabbit.commons.JcrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.*;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import java.lang.reflect.Array;
import java.util.*;

public class NodeObjectMapperImpl implements NodeObjectMapper {

    public static final Logger logger = LoggerFactory.getLogger(NodeObjectMapperImpl.class);

    private final Injector injector;

    @Inject
    public NodeObjectMapperImpl(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object map(Node objectNode) {
        try {
            String objectClassName = objectNode.getProperty(CLASS_PROPERTY).getString();
            Class<?> objectClass = Class.forName(objectClassName);
            Object object = injector.getInstance(objectClass);
            injector.injectMembers(object);
            return map(objectClass, object, objectNode, new HashMap<String, Object>());
        } catch (ClassNotFoundException | RepositoryException | InstantiationException | IllegalAccessException e) {
            logger.error("Cannot map node {}", objectNode);
            return null;
        }
    }

    private Object map(Class<?> objectClass, Object object, Node objectNode, Map<String, Object> mappedObjects)
            throws IllegalAccessException, InstantiationException, RepositoryException, ClassNotFoundException {

        mappedObjects.put(objectNode.getIdentifier(), object);

        while (!objectClass.equals(Object.class)) {
            for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (objectNode.hasProperty(fieldName)) {
                    Property property = objectNode.getProperty(fieldName);
                    Class<? extends PropertyReader> readerClass = field.getAnnotation(Field.class).reader();
                    if (readerClass == PropertyReader.class) {
                        switch (property.getType()) {
                            case PropertyType.STRING:
                            case PropertyType.URI:
                            case PropertyType.NAME:
                            case PropertyType.BINARY:
                            case PropertyType.UNDEFINED:
                            case PropertyType.PATH:
                                field.set(object, property.getString());
                                break;
                            case PropertyType.BOOLEAN:
                                field.set(object, property.getBoolean());
                                break;
                            case PropertyType.DOUBLE:
                                field.set(object, property.getDouble());
                                break;
                            case PropertyType.LONG:
                                field.set(object, property.getLong());
                                break;
                            case PropertyType.DATE:
                                field.set(object, property.getDate());
                                break;
                            case PropertyType.DECIMAL:
                            case PropertyType.REFERENCE:
                            case PropertyType.WEAKREFERENCE:
                            default:
                                throw new AssertionError("Not implemented");
                        }
                    } else {
                        PropertyReader propertyReader = injector.getInstance(readerClass);
                        injector.injectMembers(propertyReader);
                        try {
                            field.set(object, propertyReader.read(property));
                        } catch (RepositoryException | IllegalFormatException e) {
                            logger.error("Cannot set field {}, {}", fieldName, e);
                        }
                    }
                } else if (objectNode.hasNode(fieldName)) {
                    Node propertyNode = objectNode.getNode(fieldName);
                    Object property;
                    String nodeIdentifier = propertyNode.getIdentifier();
                    if (!mappedObjects.containsKey(nodeIdentifier)) {
                        Class<?> propertyClass = field.getType();
                        property = injector.getInstance(propertyClass);
                        injector.injectMembers(property);
                        map(propertyClass, property, propertyNode, mappedObjects);
                    } else {
                        property = mappedObjects.get(nodeIdentifier);
                    }
                    field.set(object, property);
                } else if (field.isAnnotationPresent(Collection.class)) {
                    // @todo add Query parameter to replace the query altogether
                    if (!field.getType().isArray()) {
                        throw new RuntimeException("Collection annotation can only be applied to array properties");
                    }
                    Class<?> itemSuperclass = field.getType().getComponentType();
                    List<Object> items = new ArrayList<>();
                    // @todo add clever parser that would add scope constraints
                    String expression = "SELECT * FROM [nt:base] WHERE [handlebars:mixin] = '"
                            + itemSuperclass.getName() + "'";
                    Session session = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE);
                    QueryManager queryManager = session.getWorkspace().getQueryManager();
                    Query query = queryManager.createQuery(expression, Query.JCR_SQL2);
                    QueryResult result = query.execute();
                    for (Row row : JcrUtils.getRows(result)) {
                        Node itemNode = row.getNode();
                        String nodeIdentifier = itemNode.getIdentifier();
                        Object item;
                        if (!mappedObjects.containsKey(nodeIdentifier)) {
                            String itemClassName = itemNode.getProperty(CLASS_PROPERTY).getString();
                            Class<?> itemClass = Class.forName(itemClassName);
                            item = injector.getInstance(itemClass);
                            injector.injectMembers(item);
                            map(itemClass, item, itemNode, mappedObjects);
                        } else {
                            item = mappedObjects.get(nodeIdentifier);
                        }
                        items.add(item);
                    }
                    if (items.size() > 0) {
                        field.set(object, toArray(items, itemSuperclass));
                    }
                }
            }
            objectClass = objectClass.getSuperclass();
            if (objectNode.hasProperty(PARENT_PROPERTY)) {
                String supplierId = objectNode.getProperty(PARENT_PROPERTY).getString();
                objectNode = objectNode.getSession().getNodeByIdentifier(supplierId);
            }
            map(objectClass, object, objectNode, mappedObjects);
        }
        return object;
    }

    private <T> T[] toArray(List<?> list, Class<T> type) {
        @SuppressWarnings("unchecked")
        T[] array = (T[]) Array.newInstance(type, list.size());
        int i = 0;
        for (Object item : list) {
            array[i++] = type.cast(item);
        }
        return array;
    }
}

package com.magnoliales.handlebars.mapper;

import com.google.inject.Injector;
import com.magnoliales.handlebars.annotations.Collection;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Query;
import com.magnoliales.handlebars.annotations.Value;
import com.magnoliales.handlebars.utils.PropertyReader;
import de.odysseus.el.util.SimpleContext;
import info.magnolia.context.MgnlContext;
import info.magnolia.init.MagnoliaConfigurationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.inject.Inject;
import javax.jcr.*;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeObjectMapperImpl implements NodeObjectMapper {

    public static final Logger logger = LoggerFactory.getLogger(NodeObjectMapperImpl.class);

    private final Injector injector;
    private final ExpressionFactory expressionFactory;
    private final MagnoliaConfigurationProperties properties;
    private final Map<String, Object> objectCache;

    @Inject
    public NodeObjectMapperImpl(Injector injector,
                                ExpressionFactory expressionFactory,
                                MagnoliaConfigurationProperties properties) {
        this.injector = injector;
        this.expressionFactory = expressionFactory;
        this.properties = properties;
        this.objectCache = new HashMap<>();
    }

    @Override
    public Object map(Node objectNode) {
        try {
            String objectClassName = objectNode.getProperty(NodeObjectMapper.CLASS_PROPERTY).getString();
            Class<?> objectClass = Class.forName(objectClassName);
            Object object = createBareObject(objectClass);
            map(objectClass, object, objectNode);
            return object;
        } catch (RepositoryException | ClassNotFoundException | IllegalAccessException e) {
            logger.error("Cannot map node {}", objectNode, e);
            return null;
        }
    }

    private void map(Class<?> objectClass,
                     Object object,
                     Node objectNode)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        String cacheKey = objectNode.getIdentifier();
        if (objectCache.containsKey(cacheKey)) {
            return;
        }

        objectCache.put(cacheKey, object);

        SimpleContext context = null;

        while (objectClass != Object.class) {
            for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (objectNode.hasProperty(fieldName)) {
                    mapProperty(field, object, objectNode.getProperty(fieldName));
                } else if (objectNode.hasNode(fieldName)) {
                    mapNode(field, object, objectNode.getNode(fieldName));
                } else if (field.isAnnotationPresent(Query.class)) {
                    if (context == null) {
                        context = initContext(object, objectNode);
                    }
                    mapQuery(field, object, objectNode, context);
                } else if (field.isAnnotationPresent(Value.class)) {
                    if (context == null) {
                        context = initContext(object, objectNode);
                    }
                    mapValue(field, object, context);
                } else if (field.isAnnotationPresent(Collection.class)) {
                    mapChildren(field, object, objectNode);
                }
            }
            objectClass = objectClass.getSuperclass();
            objectNode = getParentNode(objectNode);
        }
    }

    private SimpleContext initContext(Object object, Node objectNode) {
        SimpleContext context = new SimpleContext();
        context.setVariable("node", expressionFactory.createValueExpression(objectNode, objectNode.getClass()));
        context.setVariable("this", expressionFactory.createValueExpression(object, object.getClass()));
        context.setVariable("properties", expressionFactory.createValueExpression(properties, properties.getClass()));
        context.setVariable("parameters", expressionFactory.createValueExpression(MgnlContext.getParameters(), Map.class));
        return context;
    }

    private void mapProperty(java.lang.reflect.Field field,
                             Object object,
                             Property property)
            throws RepositoryException, IllegalAccessException, ClassNotFoundException {

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
                    field.set(object, property.getDecimal());
                    break;
                case PropertyType.REFERENCE:
                case PropertyType.WEAKREFERENCE:
                    Node referencedNode = property.getSession().getNodeByIdentifier(property.getString());
                    mapNode(field, object, referencedNode);
                    break;
                default:
                    throw new AssertionError("Not implemented");
            }
        } else {
            PropertyReader propertyReader = injector.getInstance(readerClass);
            injector.injectMembers(propertyReader);
            field.set(object, propertyReader.read(property, this));
        }
    }

    private void mapNode(java.lang.reflect.Field field,
                         Object object,
                         Node subNode)
            throws ClassNotFoundException, RepositoryException, IllegalAccessException {

        Class<?> subObjectClass = field.getType();
        Object subObject = createBareObject(subObjectClass);
        map(subObjectClass, subObject, subNode);
        field.set(object, subObject);
    }

    private void mapValue(java.lang.reflect.Field field,
                          Object object,
                          SimpleContext context)
            throws IllegalAccessException {

        String expression = field.getAnnotation(Value.class).value();
        ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, String.class);
        field.set(object, valueExpression.getValue(context));
    }

    private void mapQuery(java.lang.reflect.Field field,
                          Object object,
                          Node objectNode,
                          SimpleContext context)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        if (!field.getType().isArray()) {
            throw new RepositoryException("Non array type is annotated with query");
        }
        Class<?> itemSuperclass = field.getType().getComponentType();

        String expression = field.getAnnotation(Query.class).value();
        ValueExpression valueExpression = expressionFactory.createValueExpression(context, expression, String.class);
        String interpolatedExpression = (String) valueExpression.getValue(context);

        Session session = objectNode.getSession();
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        QueryResult result = queryManager
                .createQuery(interpolatedExpression, javax.jcr.query.Query.JCR_SQL2).execute();
        field.set(object, readArray(itemSuperclass, result.getNodes()));
    }

    private void mapChildren(java.lang.reflect.Field field,
                             Object object,
                             Node objectNode)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        if (!field.getType().isArray()) {
            throw new RepositoryException("Non array type is annotated with collection");
        }
        Class<?> itemSuperclass = field.getType().getComponentType();
        field.set(object, readArray(itemSuperclass, objectNode.getNodes()));
    }

    private Object createBareObject(Class<?> objectClass)
            throws RepositoryException, ClassNotFoundException {

        return injector.getInstance(objectClass);
    }

    private Node getParentNode(Node node) throws RepositoryException {
        if (node.hasProperty(PARENT_PROPERTY)) {
            String supplierId = node.getProperty(PARENT_PROPERTY).getString();
            return node.getSession().getNodeByIdentifier(supplierId);
        } else {
            return node;
        }
    }

    private <T> T[] readArray(Class<T> type, NodeIterator iterator)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {
        List<Object> items = new ArrayList<>();
        while (iterator.hasNext()) {
            Node itemNode = iterator.nextNode();
            String itemClassName = itemNode.getProperty(CLASS_PROPERTY).getString();
            Class<?> itemClass = Class.forName(itemClassName);
            String cacheKey = itemNode.getIdentifier();
            if (type.isAssignableFrom(itemClass)) {
                Object item;
                if (objectCache.containsKey(cacheKey)) {
                    item = objectCache.get(cacheKey);
                } else {
                    item = createBareObject(itemClass);
                    map(itemClass, item, itemNode);
                }
                items.add(item);
            }
        }
        return toArray(items, type);
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

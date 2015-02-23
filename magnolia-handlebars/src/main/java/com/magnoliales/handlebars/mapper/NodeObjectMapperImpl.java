package com.magnoliales.handlebars.mapper;

import com.google.inject.Injector;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Query;
import com.magnoliales.handlebars.annotations.Value;
import com.magnoliales.handlebars.utils.PropertyReader;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.inject.Inject;
import javax.jcr.*;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
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
            String objectClassName = objectNode.getProperty(NodeObjectMapper.CLASS_PROPERTY).getString();
            Class<?> objectClass = Class.forName(objectClassName);
            Object object = createBareObject(objectClass);
            map(objectClass, object, objectNode, new HashMap<String, Object>());
            return object;
        } catch (RepositoryException | ClassNotFoundException | IllegalAccessException e) {
            logger.error("Cannot map node {}", objectNode, e);
            return null;
        }
    }

    private void map(Class<?> objectClass,
                     Object object,
                     Node objectNode,
                     Map<String, Object> objectCache)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        String cacheKey = objectNode.getIdentifier();
        if (objectCache.containsKey(cacheKey)) {
            return;
        }

        objectCache.put(cacheKey, object);

        while (objectClass != Object.class) {
            for (java.lang.reflect.Field field : objectClass.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (objectNode.hasProperty(fieldName)) {
                    mapProperty(field, object, objectNode.getProperty(fieldName), objectCache);
                } else if (objectNode.hasNode(fieldName)) {
                    mapNode(field, object, objectNode.getNode(fieldName), objectCache);
                } else if (field.isAnnotationPresent(Query.class)) {
                    mapQuery(field, object, objectNode, objectCache);
                } else if (field.isAnnotationPresent(Value.class)) {
                    mapValue(field, object, objectNode);
                } else if (field.getType().isArray()) {
                    mapChildren(field, object, objectNode, objectCache);
                }
            }
            objectClass = objectClass.getSuperclass();
            objectNode = getParentNode(objectNode);
        }
    }

    private void mapProperty(java.lang.reflect.Field field,
                             Object object,
                             Property property,
                             Map<String, Object> objectCache)
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
                    mapNode(field, object, referencedNode, objectCache);
                    break;
                default:
                    throw new AssertionError("Not implemented");
            }
        } else {
            PropertyReader propertyReader = injector.getInstance(readerClass);
            injector.injectMembers(propertyReader);
            field.set(object, propertyReader.read(property));
        }
    }

    private void mapNode(java.lang.reflect.Field field,
                         Object object,
                         Node subNode,
                         Map<String, Object> objectCache)
            throws ClassNotFoundException, RepositoryException, IllegalAccessException {

        Class<?> subObjectClass = field.getType();
        Object subObject = createBareObject(subObjectClass);
        map(subObjectClass, subObject, subNode, objectCache);
        field.set(object, subObject);
    }

    private void mapValue(java.lang.reflect.Field field,
                          Object object,
                          Node objectNode)
            throws IllegalAccessException {

        String expression = field.getAnnotation(Value.class).value();
        Properties properties = new Properties();
        properties.setProperty("javax.el.methodInvocations", "true");


        ExpressionFactory factory = new ExpressionFactoryImpl(properties);
        SimpleContext context = new SimpleContext();
        context.setVariable("node", factory.createValueExpression(objectNode, objectNode.getClass()));
        context.setVariable("this", factory.createValueExpression(object, object.getClass()));

        ValueExpression valueExpression =
                factory.createValueExpression(context, expression, String.class);
        field.set(object, valueExpression.getValue(context));
    }

    private void mapQuery(java.lang.reflect.Field field,
                          Object object,
                          Node objectNode,
                          Map<String, Object> objectCache)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        if (!field.getType().isArray()) {
            throw new RepositoryException("Non array type is annotated with query");
        }
        Class<?> itemSuperclass = field.getType().getComponentType();

        String expression = field.getAnnotation(Query.class).value();
        Properties properties = new Properties();
        properties.setProperty("javax.el.methodInvocations", "true");

        ExpressionFactory factory = new ExpressionFactoryImpl(properties);
        SimpleContext context = new SimpleContext();
        context.setVariable("node", factory.createValueExpression(objectNode, objectNode.getClass()));
        context.setVariable("this", factory.createValueExpression(object, object.getClass()));

        ValueExpression valueExpression =
                factory.createValueExpression(context, expression, String.class);
        String interpolatedExpression = (String) valueExpression.getValue(context);

        Session session = objectNode.getSession();
        QueryManager queryManager = session.getWorkspace().getQueryManager();
        QueryResult result = queryManager
                .createQuery(interpolatedExpression, javax.jcr.query.Query.JCR_SQL2).execute();
        field.set(object, readArray(itemSuperclass, result.getNodes(), objectCache));
    }

    private void mapChildren(java.lang.reflect.Field field,
                             Object object,
                             Node objectNode,
                             Map<String, Object> objectCache)
            throws RepositoryException, ClassNotFoundException, IllegalAccessException {

        Class<?> itemSuperclass = field.getType().getComponentType();
        field.set(object, readArray(itemSuperclass, objectNode.getNodes(), objectCache));
    }

    private Object createBareObject(Class<?> objectClass)
            throws RepositoryException, ClassNotFoundException {

        Object object = injector.getInstance(objectClass);
        injector.injectMembers(object);
        return object;
    }

    private Node getParentNode(Node node) throws RepositoryException {
        if (node.hasProperty(PARENT_PROPERTY)) {
            String supplierId = node.getProperty(PARENT_PROPERTY).getString();
            return node.getSession().getNodeByIdentifier(supplierId);
        } else {
            return node;
        }
    }

    private <T> T[] readArray(Class<T> type, NodeIterator iterator, Map<String, Object> objectCache)
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
                    map(itemClass, item, itemNode, objectCache);
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

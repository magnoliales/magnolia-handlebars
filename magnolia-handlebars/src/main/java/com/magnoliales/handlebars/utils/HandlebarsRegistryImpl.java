package com.magnoliales.handlebars.utils;

import com.magnoliales.handlebars.annotations.Template;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HandlebarsRegistryImpl implements HandlebarsRegistry {

    private static Logger logger = LoggerFactory.getLogger(HandlebarsRegistryImpl.class);

    private Mapper mapper;

    public void init(String... namespaces) {

        if (mapper != null) {
            throw new IllegalStateException("Mapper is already initialized");
        }

        Set<Class> nodeClasses = new HashSet<>();
        for (String namespace : namespaces) {

            logger.info("Processing the namespace '{}'", namespace);
            Reflections reflections = new Reflections(namespace);

            Set<Class<?>> templateTypes = reflections.getTypesAnnotatedWith(Template.class);
            for (Class<?> templateType : templateTypes) {
                nodeClasses.add(templateType);
            }

            nodeClasses.addAll(reflections.getTypesAnnotatedWith(Node.class));
        }

        logger.info("Registering the node types '{}'", nodeClasses);
        mapper = new AnnotationMapperImpl(new ArrayList<Class>(nodeClasses));
    }

    public Mapper getMapper() {
        if (mapper == null) {
            throw new IllegalStateException("Mapper is not initialized yet.");
        }
        return mapper;
    }

    private void registerTemplate(Class<?> templateType) {
        System.out.println("Registering template class " + templateType);

        // @todo register the templates as well as the corresponding dialogs
    }
}

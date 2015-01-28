package com.magnoliales.handlebars.mapper;

import javax.jcr.Node;

/**
 * Maps node into an object.
 *
 * The type of the object is specified by the CLASS_PROPERTY.
 * The parent object is created from a node reference by PARENT_PROPERTY
 *
 * @todo add proper namespace to properties like "handlebars:"
 * @todo add type definitions to the repository be updating the page / area / component node types
 */
public interface NodeObjectMapper {

    public static final String CLASS_PROPERTY = "handlebars:class";
    public static final String PARENT_PROPERTY = "handlebars:parent";

    Object map(Node node);
}

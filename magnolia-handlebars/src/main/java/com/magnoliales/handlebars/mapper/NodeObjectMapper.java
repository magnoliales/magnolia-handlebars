package com.magnoliales.handlebars.mapper;

import javax.jcr.Node;

/**
 * Maps node into an object.
 *
 * The type of the object is specified by the CLASS_PROPERTY.
 * The parent object is created from a node reference by PARENT_PROPERTY.
 *
 * The mapper is not connected to any particular magnolia application and
 * is solely driven by 'class' and 'parent' markers.
 */
public interface NodeObjectMapper {

    String CLASS_PROPERTY = "mgnl:template";
    String PARENT_PROPERTY = "handlebars:parent";

    Object map(Node node);
}

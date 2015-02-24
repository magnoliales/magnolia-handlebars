package com.magnoliales.handlebars.utils;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

public interface PropertyReader {
    Object read(Property property, NodeObjectMapper mapper)
            throws RepositoryException;
}

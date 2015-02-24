package com.magnoliales.handlebars.utils;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;

import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.Map;

public interface PropertyReader {
    Object read(Property property, NodeObjectMapper mapper, Map<String, Object> objectCache)
            throws RepositoryException;
}

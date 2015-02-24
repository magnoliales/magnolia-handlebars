package com.magnoliales.handlebars.utils.pages;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.utils.PropertyReader;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.Map;

public class PageReader implements PropertyReader {

    @Override
    public Object read(Property property, NodeObjectMapper mapper, Map<String, Object> objectCache)
            throws RepositoryException {
        String id = property.getString();
        Node node = property.getSession().getNodeByIdentifier(id);
        return mapper.map(node, objectCache);
    }
}

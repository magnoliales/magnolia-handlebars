package com.magnoliales.handlebars.utils.pages;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.utils.PropertyReader;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

public class PageReader implements PropertyReader {

    private final NodeObjectMapper mapper;

    @Inject
    public PageReader(NodeObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public Object read(Property property) throws RepositoryException {
        String id = property.getString();
        Node node = property.getSession().getNodeByIdentifier(id);
        return mapper.map(node);
    }
}

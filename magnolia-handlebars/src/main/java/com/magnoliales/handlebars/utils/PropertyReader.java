package com.magnoliales.handlebars.utils;

import javax.jcr.Property;
import javax.jcr.RepositoryException;

public interface PropertyReader {
    Object read(Property property) throws RepositoryException;
}

package com.magnoliales.handlebars.mapper;

import javax.jcr.Node;

public interface NodeObjectMapper {

    public static final String TEMPLATE_PROPERTY = "mgnl:template";
    public static final String SUPPLIER_PAGE_PROPERTY = "mgnl:supplierPage";

    Object map(Node node);
}

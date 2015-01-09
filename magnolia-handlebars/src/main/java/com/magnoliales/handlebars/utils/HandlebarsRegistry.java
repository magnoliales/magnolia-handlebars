package com.magnoliales.handlebars.utils;

import org.apache.jackrabbit.ocm.mapper.Mapper;

public interface HandlebarsRegistry {

    void init(String... namespaces);

    Mapper getMapper();
}

package com.magnoliales.handlebars.utils;

import com.magnoliales.handlebars.templates.HandlebarsTemplateDefinition;
import info.magnolia.rendering.template.TemplateDefinition;
import org.apache.jackrabbit.ocm.mapper.Mapper;

import java.util.List;
import java.util.Map;

public interface HandlebarsRegistry {

    void init(String... namespaces);

    List<HandlebarsTemplateDefinition> getTemplateDefinitions();

    HandlebarsTemplateDefinition getTemplateDefinition(String template);

    Map<String, String> getPagesByTemplate(String template);

    boolean pageWithTemplateExists(String template);
}

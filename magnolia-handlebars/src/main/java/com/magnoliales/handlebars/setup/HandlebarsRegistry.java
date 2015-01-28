package com.magnoliales.handlebars.setup;

import com.magnoliales.handlebars.templating.definition.HandlebarsTemplateDefinition;

import javax.jcr.Node;
import java.util.List;
import java.util.Map;

// make sure the whole thing goes through the handlebars.properties, so that we can easily extract this one bit
// the same goes for the directory and so on file:src/resources/templates, classpath:/templates

public interface HandlebarsRegistry {

    void init(String... namespaces);

    List<HandlebarsTemplateDefinition> getTemplateDefinitions();

    HandlebarsTemplateDefinition getTemplateDefinition(String template);

    HandlebarsTemplateDefinition getTemplateDefinition(Node node);

    Map<String, String> getPagesByTemplate(String template);

    boolean pageWithTemplateExists(String template);
}

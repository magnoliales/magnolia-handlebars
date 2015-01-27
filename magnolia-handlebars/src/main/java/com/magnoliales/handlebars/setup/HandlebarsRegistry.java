package com.magnoliales.handlebars.setup;

import com.magnoliales.handlebars.templating.definition.HandlebarsTemplateDefinition;

import javax.jcr.Node;
import java.util.List;
import java.util.Map;

public interface HandlebarsRegistry {

    void init(String... namespaces);

    List<HandlebarsTemplateDefinition> getTemplateDefinitions();

    HandlebarsTemplateDefinition getTemplateDefinition(String template);

    HandlebarsTemplateDefinition getTemplateDefinition(Node node);

    Map<String, String> getPagesByTemplate(String template);

    boolean pageWithTemplateExists(String template);
}

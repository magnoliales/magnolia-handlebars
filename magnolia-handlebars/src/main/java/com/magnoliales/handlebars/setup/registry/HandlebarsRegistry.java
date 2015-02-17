package com.magnoliales.handlebars.setup.registry;

import info.magnolia.rendering.template.TemplateDefinition;

import javax.jcr.Node;
import java.util.List;
import java.util.Map;

public interface HandlebarsRegistry {

    void init(String... namespaces);

    List<TemplateDefinition> getTemplateDefinitions();

    TemplateDefinition getTemplateDefinition(String template);

    TemplateDefinition getTemplateDefinition(Node node);

    Map<String, String> getPagesByTemplate(String template);

    boolean pageWithTemplateExists(String template);
}

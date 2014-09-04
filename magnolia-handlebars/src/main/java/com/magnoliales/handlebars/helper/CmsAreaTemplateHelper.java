package com.magnoliales.handlebars.helper;

import com.github.jknack.handlebars.Options;
import com.magnoliales.handlebars.annotations.ParentTemplate;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.module.blossom.template.BlossomTemplateDefinition;
import info.magnolia.objectfactory.Components;
import info.magnolia.registry.RegistrationException;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.templating.elements.AreaElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

public class CmsAreaTemplateHelper extends AbstractTemplateHelper<AreaElement> {

    private static final Logger log = LoggerFactory.getLogger(CmsInitTemplateHelper.class);

    public CharSequence apply(Object context, Options options) throws IOException {

        final AreaElement templatingElement = createTemplatingElement();
        initContentElement(options, templatingElement);

        String name = options.hash("name");
        String availableComponents = options.hash("components");
        String dialog = options.hash("dialog");
        String type = options.hash("type");
        String label = options.hash("label");
        String description = options.hash("description");
        Boolean editable = options.hash("editable");
        Map<String,Object> contextAttributes = options.hash("contextAttributes");


        BlossomTemplateDefinition templateDefinition = (BlossomTemplateDefinition) ((Map) context).get("def");
        Class<?> templateClass = templateDefinition.getHandler().getClass();
        AreaDefinition area = getAreaDefinition(name, templateClass);

        templatingElement.setArea(area);
        templatingElement.setName(name);
        templatingElement.setAvailableComponents(availableComponents);
        templatingElement.setDialog(dialog);
        templatingElement.setType(type);
        templatingElement.setLabel(label);
        templatingElement.setDescription(description);
        templatingElement.setEditable(editable);

        templatingElement.setContextAttributes(contextAttributes);

        return render(templatingElement);
    }

    private AreaDefinition getAreaDefinition(String name, Class<?> templateClass) {
        TemplateDefinitionRegistry registry = Components.getComponent(TemplateDefinitionRegistry.class);
        String templateId = templateClass.getAnnotation(Template.class).id();
        TemplateDefinition templateDefinition;
        try {
            templateDefinition = registry.getTemplateDefinition(templateId);
        } catch (RegistrationException e) {
            log.error("Cannot fetch template definition from registry", e);
            return null;
        }
        if (templateDefinition.getAreas().containsKey(name)) {
            return templateDefinition.getAreas().get(name);
        }
        Class<?> parentTemplateClass = templateClass.getAnnotation(ParentTemplate.class).value();
        return getAreaDefinition(name, parentTemplateClass);
    }
}

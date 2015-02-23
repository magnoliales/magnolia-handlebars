package com.magnoliales.handlebars.ui.fields.template.field;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.rendering.definition.HandlebarsPageDefinition;
import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import com.magnoliales.handlebars.ui.fields.template.PageClass;
import com.vaadin.data.Item;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.configured.ConfiguredTemplateAvailability;
import info.magnolia.rendering.template.configured.ConfiguredTemplateDefinition;
import info.magnolia.ui.form.field.definition.FieldDefinition;
import info.magnolia.ui.form.field.factory.AbstractFieldFactory;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;

import javax.inject.Inject;
import javax.jcr.Node;

public class PageClassFieldFactory<D extends FieldDefinition>
        extends AbstractFieldFactory<PageClassFieldDefinition, PageClass> {

    private HandlebarsRegistry handlebarsRegistry;
    private Node parentNode;
    private TemplateDefinition parentTemplate;

    @Inject
    public PageClassFieldFactory(PageClassFieldDefinition definition,
                                 Item relatedFieldItem,
                                 HandlebarsRegistry handlebarsRegistry) {
        super(definition, relatedFieldItem);
        this.handlebarsRegistry = handlebarsRegistry;
        this.parentNode = ((JcrNodeAdapter) relatedFieldItem).getJcrItem();
        String template = PropertyUtil.getString(parentNode, NodeObjectMapper.CLASS_PROPERTY);
        if (template == null) {
            parentTemplate = new ConfiguredTemplateDefinition(new ConfiguredTemplateAvailability());
        } else {
            parentTemplate = handlebarsRegistry.getTemplateDefinition(template);
        }
    }

    @Override
    protected PageClassField createFieldComponent() {
        return new PageClassField(handlebarsRegistry, parentNode, parentTemplate);
    }

    @Override
    protected Class<?> getFieldType() {
        return PageClassField.class;
    }

    @Override
    protected Class<?> getDefinitionType() {
        return PageClassFieldDefinition.class;
    }
}
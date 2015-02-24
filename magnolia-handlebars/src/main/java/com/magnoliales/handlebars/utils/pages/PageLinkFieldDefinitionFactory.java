package com.magnoliales.handlebars.utils.pages;

import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import info.magnolia.ui.form.field.converter.BaseIdentifierToPathConverter;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;

public class PageLinkFieldDefinitionFactory implements FieldDefinitionFactory {

    public LinkFieldDefinition getInstance() {
        LinkFieldDefinition definition = new LinkFieldDefinition();
        definition.setAppName("pages");
        definition.setTargetWorkspace("website");
        definition.setIdentifierToPathConverter(new BaseIdentifierToPathConverter());
        return definition;
    }
}

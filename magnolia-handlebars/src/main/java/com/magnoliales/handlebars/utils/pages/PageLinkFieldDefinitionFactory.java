package com.magnoliales.handlebars.utils.pages;

import info.magnolia.ui.form.field.converter.BaseIdentifierToPathConverter;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;

public class PageLinkFieldDefinitionFactory {

    public LinkFieldDefinition getInstance() {
        LinkFieldDefinition definition = new LinkFieldDefinition();
        definition.setAppName("pages");
        definition.setTargetWorkspace("website");
        definition.setIdentifierToPathConverter(new BaseIdentifierToPathConverter());
        return definition;
    }
}

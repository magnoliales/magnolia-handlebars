package com.magnoliales.handlebars.utils.dam;

import com.magnoliales.handlebars.utils.FieldDefinitionFactory;
import info.magnolia.dam.app.assets.field.translator.AssetCompositeIdKeyTranslator;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;

public class DamLinkFieldDefinitionFactory implements FieldDefinitionFactory {

    public LinkFieldDefinition getInstance() {
        LinkFieldDefinition definition = new LinkFieldDefinition();
        definition.setAppName("assets");
        definition.setTargetWorkspace("dam");
        definition.setIdentifierToPathConverter(new AssetCompositeIdKeyTranslator());
        return definition;
    }
}

package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.utils.dam.AssetLinkFieldDefinitionFactory;
import com.magnoliales.handlebars.utils.dam.AssetReader;
import info.magnolia.dam.api.Asset;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "example-page", singleton = true)
public class ExamplePage {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String description;

    @Field(factory = AssetLinkFieldDefinitionFactory.class, reader = AssetReader.class)
    private Asset image;

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Asset getImage() {
        return image;
    }
}


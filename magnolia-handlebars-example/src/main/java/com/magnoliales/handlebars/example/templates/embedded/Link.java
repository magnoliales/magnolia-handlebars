package com.magnoliales.handlebars.example.templates.embedded;

import com.magnoliales.handlebars.annotations.Composite;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Composite
public class Link {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    public String getTitle() {
        return title;
    }
}

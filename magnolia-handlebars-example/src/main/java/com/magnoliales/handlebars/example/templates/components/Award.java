package com.magnoliales.handlebars.example.templates.components;

import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Component(templateScript = "components/award")
public final class Award {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    public String getTitle() {
        return title;
    }
}
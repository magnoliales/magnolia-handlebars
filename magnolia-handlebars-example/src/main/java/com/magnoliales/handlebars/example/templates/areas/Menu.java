package com.magnoliales.handlebars.example.templates.areas;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Area(templateScript = "areas/menu")
public final class Menu {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    public String getTitle() {
        return title;
    }
}

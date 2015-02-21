package com.magnoliales.handlebars.example.templates.areas;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.example.templates.components.Award;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Area(templateScript = "areas/awards")
public final class Awards {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    private Award[] items;

    public String getTitle() {
        return title;
    }

    public Award[] getItems() {
        return items;
    }
}

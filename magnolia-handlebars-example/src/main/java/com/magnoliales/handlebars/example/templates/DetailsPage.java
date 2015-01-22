package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "details-page")
public class DetailsPage extends AbstractDetailsPage {

    @Field(inherits = true)
    private String title;

    @Field(definition = TextFieldDefinition.class, settings = "{}")
    private String greeting;

    @Override
    public String getGreeting() {
        return greeting;
    }
}

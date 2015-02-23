package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;
import com.magnoliales.handlebars.example.templates.areas.Awards;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "details-page")
public class DetailsPage extends AbstractDetailsPage {

    @Value("${node.path}")
    private String path;

    @Field(inherits = true)
    private String title;

    @Field(definition = TextFieldDefinition.class)
    private String greeting;

    private Awards award;

    public String getPath() {
        return path;
    }

    @Override
    public String getGreeting() {
        return greeting;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

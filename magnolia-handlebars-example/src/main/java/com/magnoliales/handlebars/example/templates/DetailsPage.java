package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "details-page")
public class DetailsPage extends AbstractDetailsPage {

    @Value(inherits = true)
    private String title;

    @Value(fieldDefinition = TextFieldDefinition.class, settings = "{}")
    private String greeting;

    @Override
    public String getGreeting() {
        return greeting;
    }

    public String getTitle() {
        return title + "(" + super.getTitle() + ")";
    }


}

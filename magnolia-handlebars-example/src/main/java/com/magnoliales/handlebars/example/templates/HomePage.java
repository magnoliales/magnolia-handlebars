package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Composite;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Value(fieldDefinition = TextFieldDefinition.class, settings = "{}")
    private String title;

    @Value(fieldDefinition = TextFieldDefinition.class)
    private String name;

    @Composite
    private Meta meta;

    public String getTitle() {
        return title;
    }

    public String getGreeting() {
        return "Hello";
    }

    public String getName() {
        return name;
    }

    public Meta getMeta() {
        return meta;
    }

    public static class Meta {

        @Value(fieldDefinition = TextFieldDefinition.class)
        private String name;

        @Value(fieldDefinition = TextFieldDefinition.class)
        private String image;

    }
}

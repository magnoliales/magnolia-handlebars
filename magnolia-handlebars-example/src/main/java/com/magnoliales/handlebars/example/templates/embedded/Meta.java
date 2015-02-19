package com.magnoliales.handlebars.example.templates.embedded;

import com.magnoliales.handlebars.annotations.Field;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

public final class Meta {

    @Field(definition = TextFieldDefinition.class)
    private String title;

    private Facebook facebook;

    @Field(definition = TextFieldDefinition.class)
    private String image;

    public static final class Facebook {

        @Field(definition = TextFieldDefinition.class)
        private String title;

        @Field(definition = TextFieldDefinition.class)
        private String image;
    }
}


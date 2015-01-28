package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

import java.util.Collection;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Field(definition = TextFieldDefinition.class, settings = "{}")
    private String title;

    @Field(definition = TextFieldDefinition.class)
    private String addressee;

    private Meta meta;

    private Awards awards;

    public String getTitle() {
        return title;
    }

    public String getGreeting() {
        return "Hello";
    }

    public String getAddressee() {
        return addressee;
    }

    public Meta getMeta() {
        return meta;
    }

    public final static class Meta {

        @Field(definition = TextFieldDefinition.class)
        private String title;

        private Facebook facebook;

        @Field(definition = TextFieldDefinition.class)
        private String image;

        public final static class Facebook {

            @Field(definition = TextFieldDefinition.class)
            private String title;

            @Field(definition = TextFieldDefinition.class)
            private String image;
        }
    }

    @Area(templateScript = "areas/awards")
    public final static class Awards {

        @Field(definition = TextFieldDefinition.class)
        private String title;

        private Award[] items;

        @Component(templateScript = "components/award")
        public final static class Award {

            @Field(definition = TextFieldDefinition.class)
            private String title;
        }
    }
}

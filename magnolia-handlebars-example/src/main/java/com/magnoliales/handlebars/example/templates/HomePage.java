package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.example.templates.areas.Awards;
import com.magnoliales.handlebars.example.templates.embedded.Meta;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

import javax.inject.Inject;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2 }")
    private String title;

    @Field(definition = TextFieldDefinition.class)
    private String addressee;

    // @Field(definition = LinkFieldDefinition.class, settings = "{ appName : 'assets', targetWorkspace : 'dam' }")
    private String heroImageLink;

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
}

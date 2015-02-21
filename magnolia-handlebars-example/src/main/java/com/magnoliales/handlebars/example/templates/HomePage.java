package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Collection;
import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.example.templates.areas.Awards;
import com.magnoliales.handlebars.example.templates.embedded.Meta;
import com.magnoliales.handlebars.example.templates.mixins.IndexPage;
import com.magnoliales.handlebars.utils.dam.AssetReader;
import com.magnoliales.handlebars.utils.dam.DamLinkFieldDefinitionFactory;
import info.magnolia.dam.api.Asset;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "home-page", singleton = true)
public class HomePage implements IndexPage {

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2 }")
    private String title;

    @Field(definition = TextFieldDefinition.class)
    private String addressee;

    @Field(factory = DamLinkFieldDefinitionFactory.class, reader = AssetReader.class)
    private Asset heroImage;

    @Collection
    private IndexPage[] index;

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

    public Asset getHeroImage() {
        return heroImage;
    }

    public IndexPage[] getIndex() {
        return index;
    }
}

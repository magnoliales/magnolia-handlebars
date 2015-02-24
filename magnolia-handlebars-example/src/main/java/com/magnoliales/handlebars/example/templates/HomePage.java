package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Field;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Query;
import com.magnoliales.handlebars.annotations.Value;
import com.magnoliales.handlebars.example.templates.areas.Awards;
import com.magnoliales.handlebars.example.templates.embedded.Meta;
import com.magnoliales.handlebars.utils.dam.AssetReader;
import com.magnoliales.handlebars.utils.dam.AssetLinkFieldDefinitionFactory;
import com.magnoliales.handlebars.utils.pages.PageLinkFieldDefinitionFactory;
import com.magnoliales.handlebars.utils.pages.PageReader;
import info.magnolia.dam.api.Asset;
import info.magnolia.ui.form.field.definition.TextFieldDefinition;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Value("${node.path}")
    private String path;

    @Field(definition = TextFieldDefinition.class, settings = "{ rows : 2, maxLength : 100 }")
    private String title;

    @Field(definition = TextFieldDefinition.class)
    private String addressee;

    @Field(factory = AssetLinkFieldDefinitionFactory.class, reader = AssetReader.class)
    private Asset heroImage;

    private Meta meta;

    private Awards awards;

    @Query("SELECT * FROM [mgnl:page] AS page WHERE CONTAINS(page.*, '\"${parameters.query}\"~10')")
    private DetailsPage[] children;

    @Field(factory = PageLinkFieldDefinitionFactory.class, reader = PageReader.class)
    private Object page;

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

    public DetailsPage[] getChildren() {
        return children;
    }
}

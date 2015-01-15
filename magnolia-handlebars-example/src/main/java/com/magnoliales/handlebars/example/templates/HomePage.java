package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Property;

import java.util.Date;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Property
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return "World";
    }

    public String getDate() { return new Date().toString(); }
}

package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;

import java.util.Date;

@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Value
    private String title;

    public String getTitle() {
        return title;
    }

    public String getName() {
        return "World";
    }

    public String getDate() { return new Date().toString(); }
}

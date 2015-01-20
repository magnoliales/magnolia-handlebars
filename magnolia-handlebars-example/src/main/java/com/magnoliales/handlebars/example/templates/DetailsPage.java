package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.annotations.Value;

@Page(templateScript = "details-page", parents = { HomePage.class })
public class DetailsPage extends HomePage {

    @Value
    private String title;

    public String getTitle() {
        return title + "(" + super.getTitle() + ")";
    }
}

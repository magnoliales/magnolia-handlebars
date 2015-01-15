package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;

@Page(templateScript = "details-page", parents = { HomePage.class })
public class DetailsPage extends HomePage {

    public String getParentTitle() {
        return super.getTitle();
    }
}

package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;

@Page(templateScript = "sub-details-page", parents = { DetailsPage.class })
public class SubDetailsPage extends DetailsPage {
}

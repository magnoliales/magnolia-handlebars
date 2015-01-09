package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Template;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
@Template(template = "sub-details-page", parents = { DetailsPage.class })
public class SubDetailsPage extends DetailsPage {
}

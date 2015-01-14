package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "mgnl:page")
@Page(templateScript = "sub-details-page", parents = { DetailsPage.class })
public class SubDetailsPage extends DetailsPage {
}

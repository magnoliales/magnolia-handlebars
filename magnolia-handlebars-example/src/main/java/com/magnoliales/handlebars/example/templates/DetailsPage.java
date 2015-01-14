package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "mgnl:page")
@Page(templateScript = "details-page", parents = { HomePage.class })
public class DetailsPage extends HomePage {

}

package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Template;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
@Template(template = "details-page", parents = { HomePage.class })
public class DetailsPage extends HomePage {


}

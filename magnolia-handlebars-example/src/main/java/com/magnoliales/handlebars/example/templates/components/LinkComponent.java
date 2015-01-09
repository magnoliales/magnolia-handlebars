package com.magnoliales.handlebars.example.templates.components;

import com.magnoliales.handlebars.annotations.Component;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
@Component(template = "components/link")
public class LinkComponent {

    @Field
    private String url;

    @Field
    private String text;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getMarkup() {
        return "<a href='" + url + "'>" + text + "</a>";
    }
}
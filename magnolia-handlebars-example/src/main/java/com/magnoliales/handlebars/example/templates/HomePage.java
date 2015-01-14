package com.magnoliales.handlebars.example.templates;

import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.example.templates.areas.HeaderArea;
import org.apache.jackrabbit.ocm.manager.beanconverter.impl.InlineBeanConverterImpl;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Bean;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.util.Date;

@Node(jcrType = "mgnl:page")
@Page(templateScript = "home-page", singleton = true)
public class HomePage {

    @Field
    private String title;

    @Bean(converter = InlineBeanConverterImpl.class)
    private HeaderArea header;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public HeaderArea getHeader() {
        return header;
    }

    public void setHeader(HeaderArea header) {
        this.header = header;
    }

    public String getName() {
        return "World";
    }

    public String getDate() { return new Date().toString(); }
}

package com.magnoliales.handlebars.example.templates.areas;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.example.templates.components.HeaderComponent;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import java.util.ArrayList;
import java.util.List;

@Node
@Area(template = "areas/header")
public class HeaderArea {

    @Collection
    private List<HeaderComponent> components = new ArrayList<>();

    public List<HeaderComponent> getComponents() {
        return components;
    }

    public void setComponents(List<HeaderComponent> components) {
        this.components = components;
    }
}

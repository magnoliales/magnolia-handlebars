package com.magnoliales.handlebars.example.components;

import org.springframework.web.bind.annotation.RequestMapping;

// @Controller
// @Template(id = TextComponent.ID, title = "Text component", dialog = TextComponentDialog.ID)
public class TextComponent {

    public static final String ID = "handlebars-example:components/text";

    @RequestMapping("/text")
    public String render() {
        return "com/magnoliales/handlebars/example/components/text";
    }
}

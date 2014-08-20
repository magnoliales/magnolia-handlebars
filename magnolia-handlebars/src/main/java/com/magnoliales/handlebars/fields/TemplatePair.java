package com.magnoliales.handlebars.fields;

public class TemplatePair {

    private String template;
    private String parentTemplate;

    public TemplatePair(String template, String parentTemplate) {
        this.template = template;
        this.parentTemplate = parentTemplate;
    }

    public String getTemplate() {
        return template;
    }

    public String getParentTemplate() {
        return parentTemplate;
    }
}

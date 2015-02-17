package com.magnoliales.handlebars.ui.fields.template;

import javax.annotation.Nullable;

public class PageClass {

    private String pageClassName;
    private String parentNodeId;

    public PageClass(String pageClassName, String parentNodeId) {
        this.pageClassName = pageClassName;
        this.parentNodeId = parentNodeId;
    }

    @Nullable
    public String getPageClassName() {
        return pageClassName;
    }

    @Nullable
    public String getParentNodeId() {
        return parentNodeId;
    }
}

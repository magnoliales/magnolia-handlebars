package com.magnoliales.handlebars.fields.template;

import javax.annotation.Nullable;

public class TemplateAndSupplierPage {

    private String template;
    private String supplierPage;

    public TemplateAndSupplierPage() {
    }

    public TemplateAndSupplierPage(String template, String supplierPage) {
        this.template = template;
        this.supplierPage = supplierPage;
    }

    @Nullable
    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Nullable
    public String getSupplierPage() {
        return supplierPage;
    }

    public void setSupplierPage(String supplierPage) {
        this.supplierPage = supplierPage;
    }
}

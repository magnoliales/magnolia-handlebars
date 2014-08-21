package com.magnoliales.handlebars.fields;

import info.magnolia.ui.form.field.converter.BaseIdentifierToPathConverter;
import info.magnolia.ui.form.field.converter.IdentifierToPathConverter;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;

public class SupplierPageLinkDefinition extends LinkFieldDefinition {

    @Override
    public String getAppName() {
        return "supplierPages";
    }

    @Override
    public IdentifierToPathConverter getIdentifierToPathConverter() {
        return new BaseIdentifierToPathConverter();
    }
}

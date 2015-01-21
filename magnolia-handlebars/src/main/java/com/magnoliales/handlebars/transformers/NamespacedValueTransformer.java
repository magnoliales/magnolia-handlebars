package com.magnoliales.handlebars.transformers;

import com.vaadin.data.Item;
import com.vaadin.data.util.PropertysetItem;
import info.magnolia.ui.form.field.definition.ConfiguredFieldDefinition;
import info.magnolia.ui.form.field.transformer.basic.BasicTransformer;

public class NamespacedValueTransformer extends BasicTransformer<Object> {

    public NamespacedValueTransformer(Item relatedFormItem,
                                      ConfiguredFieldDefinition definition,
                                      Class<Object> type) {
        super(relatedFormItem, definition, type);
    }

    @Override
    public Object readFromItem() {
        return null;
    }

    @Override
    public void writeToItem(Object newValue) {
        System.out.println(newValue.toString());
        // I have a value now go fuck yourself
    }
}

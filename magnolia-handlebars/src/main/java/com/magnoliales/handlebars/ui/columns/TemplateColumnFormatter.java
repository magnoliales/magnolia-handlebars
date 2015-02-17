package com.magnoliales.handlebars.ui.columns;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.ui.workbench.column.AbstractColumnFormatter;
import info.magnolia.ui.workbench.column.definition.AbstractColumnDefinition;

import javax.inject.Inject;

public class TemplateColumnFormatter extends AbstractColumnFormatter<AbstractColumnDefinition> {

    private SimpleTranslator translator;

    @Inject
    public TemplateColumnFormatter(AbstractColumnDefinition definition, SimpleTranslator translator) {
        super(definition);
        this.translator = translator;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        Item item = source.getItem(itemId);
        Property property = (item == null) ? null : item.getItemProperty(columnId);
        if (property != null) {
            String template = (String) property.getValue();
            if (template != null) {
                return translator.translate("pages." + template);
            }
        }
        return null;
    }
}

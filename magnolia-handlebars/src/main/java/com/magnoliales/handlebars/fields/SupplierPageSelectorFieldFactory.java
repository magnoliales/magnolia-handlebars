package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import info.magnolia.ui.form.FormTab;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;
import info.magnolia.ui.vaadin.form.FormSection;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Map;

public class SupplierPageSelectorFieldFactory extends SelectFieldFactory<SupplierPageSelectorDefinition> {

    private final TemplateUtils utils;

    @Inject
    public SupplierPageSelectorFieldFactory(SupplierPageSelectorDefinition definition, Item relatedFieldItem,
                                            ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        utils = new TemplateUtils(applicationContextContainer);
    }

    @Override
    protected AbstractSelect createFieldComponent() {

        final AbstractSelect supplierPageSelect = super.createFieldComponent();
        supplierPageSelect.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);

        final AbstractSelect templateSelect = findTemplateSelect();
        if (templateSelect == null) {
            throw new RuntimeException("Cannot find template ComboBox");
        }
        Property.ValueChangeListener listener = getValueChangeListener(templateSelect, supplierPageSelect);
        templateSelect.addValueChangeListener(listener);
        listener.valueChange(new Field.ValueChangeEvent(templateSelect));

        supplierPageSelect.setVisible(utils.requiresParentTemplate((String) templateSelect.getValue()));
        return supplierPageSelect;
    }

    @Nullable
    private AbstractSelect findTemplateSelect() {
        FormSection formSection = ((FormTab) this.getParent()).getContainer();
        for (final Component sibling : formSection) {
            if (sibling instanceof AbstractSelect) {
                // @todo need better test for the component that I need
                return (AbstractSelect) sibling;
            }
        }
        return null;
    }

    private Property.ValueChangeListener getValueChangeListener(final AbstractSelect templateComboBox,
                                                                final AbstractSelect supplierPageSelect) {
        final Map<String, String> parentTemplates = utils.getParentTemplates();
        return new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String templateId = (String) templateComboBox.getValue();
                boolean requiresSupplierPage = parentTemplates.containsKey(templateId);
                if (requiresSupplierPage) {
                    supplierPageSelect.removeAllItems();
                    String parentTemplateId = parentTemplates.get(templateId);
                    final Map<String, String> pages = utils.findPagesUsingTemplate(parentTemplateId);
                    for (Map.Entry<String, String> entry : pages.entrySet()) {
                        supplierPageSelect.addItem(entry.getKey());
                        supplierPageSelect.setItemCaption(entry.getKey(), entry.getValue());
                    }
                    supplierPageSelect.setRequired(true);
                    supplierPageSelect.setVisible(true);
                } else {
                    supplierPageSelect.setValue(null);
                    supplierPageSelect.setRequired(false);
                    supplierPageSelect.setVisible(false);
                }
            }
        };
    }
}

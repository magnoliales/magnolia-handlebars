package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.setup.ApplicationContextAdapter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;

import javax.jcr.Session;
import javax.jcr.query.QueryManager;
import java.util.List;
import java.util.Map;

/**
 * https://documentation.magnolia-cms.com/display/DOCS/Custom+fields
 * https://dev.vaadin.com/svn/doc/book-examples/branches/vaadin-7/src/com/vaadin/book/examples/component/CustomFieldExample.java
 */
public class TemplatePairSelectorField extends CustomField<TemplatePair> {

    private final ComboBox templateComboBox;
    private final ComboBox parentTemplateComboBox;
    private final Map<String, ApplicationContextAdapter.TemplateOption> templateOptions;

    @SuppressWarnings("unchecked")
    public TemplatePairSelectorField(ApplicationContextAdapter applicationContextAdapter) {

        // QueryManager queryManager = session.getWorkspace().getQueryManager();
        templateOptions = applicationContextAdapter.getTemplateOptions();

        templateComboBox = new ComboBox();
        templateComboBox.setSizeFull();
        templateComboBox.setNullSelectionAllowed(false);
        templateComboBox.setImmediate(true);
        for (String id : templateOptions.keySet()) {
            templateComboBox.addItem(id);
            templateComboBox.setItemCaption(id, templateOptions.get(id).getTitle());
        }

        parentTemplateComboBox = new ComboBox();
        parentTemplateComboBox.setSizeFull();
        parentTemplateComboBox.setVisible(false);
    }


    @Override
    protected Component initContent() {
        VerticalLayout container = new VerticalLayout();
        container.addComponent(templateComboBox);
        container.addComponent(parentTemplateComboBox);

        ValueChangeListener listener = new ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                String templateId = (String) templateComboBox.getValue();
                ApplicationContextAdapter.TemplateOption option = templateOptions.get(templateId);
                if (option.requiresParent()) {
                    parentTemplateComboBox.setVisible(true);
                } else {
                    parentTemplateComboBox.setVisible(false);
                }

                // String parentTemplate = parentTemplateComboBox.getValue() == null ? null : (String) parentTemplateComboBox.getValue();
                // setValue(new TemplatePair(templateId, null));

                // add population thing as well, that changes the selection in the second combobox!!
            }
        };

        templateComboBox.addValueChangeListener(listener);
        // parentTemplateComboBox.addValueChangeListener(listener);
        return container;
    }

    @Override
    public Class<? extends TemplatePair> getType() {
        return TemplatePair.class;
    }

    @Override
    public void focus() {
        templateComboBox.focus();
    }
}

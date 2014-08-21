package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.form.FormTab;
import info.magnolia.ui.form.field.factory.SelectFieldFactory;
import info.magnolia.ui.vaadin.form.FormSection;
import org.apache.jackrabbit.commons.JcrUtils;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class SupplierPageSelectorFieldFactory extends SelectFieldFactory<SupplierPageSelectorDefinition> {

    final private Map<String, String> parentTemplates;

    @Inject
    public SupplierPageSelectorFieldFactory(SupplierPageSelectorDefinition definition, Item relatedFieldItem,
                                            ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        parentTemplates = getParentTemplates(applicationContextContainer.getContext());
    }

    @Override
    protected AbstractSelect createFieldComponent() {
        final AbstractSelect supplierPageComboBox = super.createFieldComponent();
        supplierPageComboBox.setVisible(false);
        supplierPageComboBox.setItemCaptionMode(AbstractSelect.ItemCaptionMode.EXPLICIT_DEFAULTS_ID);
        FormSection formSection = ((FormTab) this.getParent()).getContainer();
        for (final Component sibling : formSection) {
            if (sibling instanceof ComboBox) { // @todo need better test for the component that I need
                final ComboBox templateComboBox = (ComboBox) sibling;
                templateComboBox.addValueChangeListener(new Property.ValueChangeListener() {
                    @Override
                    public void valueChange(Property.ValueChangeEvent event) {
                        String templateId = (String) templateComboBox.getValue();
                        boolean requiresSupplierPage = parentTemplates.containsKey(templateId);
                        if (requiresSupplierPage) {
                            supplierPageComboBox.removeAllItems();
                            String parentTemplateId = parentTemplates.get(templateId);
                            final Map<String, String> pages = findPagesUsingTemplate(parentTemplateId);
                            for (String pagePath : pages.keySet()) {
                                String pageId = pages.get(pagePath);
                                supplierPageComboBox.addItem(pageId);
                                supplierPageComboBox.setItemCaption(pageId, pagePath);
                            }
                            supplierPageComboBox.setRequired(true);
                            supplierPageComboBox.setVisible(true);
                        } else {
                            supplierPageComboBox.setValue(null);
                            supplierPageComboBox.setRequired(false);
                            supplierPageComboBox.setVisible(false);
                        }
                    }
                });
                break;
            }
        }
        return supplierPageComboBox;
    }

    private Map<String, String> getParentTemplates(ApplicationContext context) {
        Map<String, String> parentTemplates = new HashMap<String, String>();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Class<?> beanClass = context.getBean(beanDefinitionName).getClass();
            if (beanClass.isAnnotationPresent(Template.class)) {
                Template template = beanClass.getAnnotation(Template.class);
                if (beanClass.isAnnotationPresent(ParentTemplate.class)) {
                    Template parentTemplate = beanClass.getAnnotation(ParentTemplate.class)
                            .value().getAnnotation(Template.class);
                    parentTemplates.put(template.id(), parentTemplate.id());
                }
            }
        }
        return parentTemplates;
    }

    private Map<String, String> findPagesUsingTemplate(String templateId) {
        Map<String, String> pages = new TreeMap<String, String>();
        String expression = "SELECT * FROM [mgnl:page] WHERE [mgnl:template] = '" + templateId + "'";
        try {
            Session session = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE);
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(expression, Query.JCR_SQL2);
            QueryResult result = query.execute();
            for (Row row : JcrUtils.getRows(result)) {
                Node node = row.getNode();
                pages.put(node.getPath(), node.getIdentifier());
            }
        } catch (LoginException e) {
            e.printStackTrace();
        } catch (InvalidQueryException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
        return pages;
    }
}

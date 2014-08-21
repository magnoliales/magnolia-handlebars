package com.magnoliales.handlebars.fields;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import com.magnoliales.handlebars.setup.ApplicationContextContainer;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import info.magnolia.module.blossom.annotation.Template;
import info.magnolia.objectfactory.ComponentProvider;
import info.magnolia.ui.api.app.AppController;
import info.magnolia.ui.api.context.UiContext;
import info.magnolia.ui.form.FormTab;
import info.magnolia.ui.form.field.definition.LinkFieldDefinition;
import info.magnolia.ui.form.field.factory.LinkFieldFactory;
import info.magnolia.ui.vaadin.form.FormSection;
import org.springframework.context.ApplicationContext;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

public class SupplierPageLinkFieldFactory extends LinkFieldFactory<SupplierPageLinkDefinition> {

    final private Map<String, String> parentTemplates;

    @Inject
    public SupplierPageLinkFieldFactory(LinkFieldDefinition definition, Item relatedFieldItem,
                                        AppController appController, UiContext uiContext,
                                        ComponentProvider componentProvider,
                                        ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem, appController, uiContext, componentProvider);
        parentTemplates = getParentTemplates(applicationContextContainer.getContext());
    }

    @Override
    protected Field<String> createFieldComponent() {
        final Field<String> component = super.createFieldComponent();
        component.setVisible(false);
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
                            component.setRequired(true);
                            component.setVisible(true);
                        } else {
                            component.setRequired(false);
                            component.setVisible(false);
                        }
                    }
                });
            }
        }
        return component;
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

    /*


    @Inject
    public SupplierPageLinkFieldFactory(SupplierPageLinkDefinition definition, Item relatedFieldItem,
                                        ApplicationContextContainer applicationContextContainer) {
        super(definition, relatedFieldItem);
        this.parentTemplates = getParentTemplates(applicationContextContainer.getContext());
    }

    @Override
    protected Class<?> getDefaultFieldType() {
        return String.class;
    }

    @Override
    protected AbstractSelect createFieldComponent() {
        final AbstractSelect supplierPageComboBox = super.createFieldComponent();
        supplierPageComboBox.setVisible(false);
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
                String title = PropertyUtil.getString(node, "title");
                if (title == null) {
                    selectBox =
                }
                pages.put(node.getPath(), ;
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
    */
}

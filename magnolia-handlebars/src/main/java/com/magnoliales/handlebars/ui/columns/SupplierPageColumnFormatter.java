package com.magnoliales.handlebars.ui.columns;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import info.magnolia.ui.vaadin.integration.jcr.JcrNodeAdapter;
import info.magnolia.ui.workbench.column.AbstractColumnFormatter;
import info.magnolia.ui.workbench.column.definition.AbstractColumnDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class SupplierPageColumnFormatter extends AbstractColumnFormatter<AbstractColumnDefinition> {

    private static final Logger logger = LoggerFactory.getLogger(SupplierPageColumnFormatter.class);

    @Inject
    public SupplierPageColumnFormatter(AbstractColumnDefinition definition) {
        super(definition);
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        Item item = source.getItem(itemId);
        Property property = (item == null) ? null : item.getItemProperty(columnId);
        if (property != null && item instanceof JcrNodeAdapter) {
            try {
                Session session = ((JcrNodeAdapter) item).getJcrItem().getSession();
                Node supplierPage = session.getNodeByIdentifier(property.toString());
                return supplierPage.getProperty("title").getString();
            } catch (RepositoryException e) {
                logger.error("Cannot fetch supplier page", e);
            }
        }
        return null;
    }
}

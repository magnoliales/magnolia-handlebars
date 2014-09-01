package com.magnoliales.handlebars.fields;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.PropertyUtil;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.workbench.column.AbstractColumnFormatter;
import info.magnolia.ui.workbench.column.definition.AbstractColumnDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class SupplierPageColumnFormatter extends AbstractColumnFormatter<AbstractColumnDefinition> {

    private static final Logger log = LoggerFactory.getLogger(SupplierPageColumnFormatter.class);

    @Inject
    public SupplierPageColumnFormatter(AbstractColumnDefinition definition) {
        super(definition);
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        Item item = source.getItem(itemId);
        Property property = (item == null) ? null : item.getItemProperty(columnId);

        if (property != null) {
            Node supplierPage = null;
            try {
                supplierPage = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE).getNodeByIdentifier(property.toString());
            } catch (RepositoryException e) {
                log.error("Cannot fetch supplier page", e);
            }
            return PropertyUtil.getString(supplierPage, "title");
        }
        return null;
    }
}

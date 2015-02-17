package com.magnoliales.handlebars.ui.availability;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import info.magnolia.context.MgnlContext;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.ui.api.availability.AbstractAvailabilityRule;
import info.magnolia.ui.vaadin.integration.jcr.JcrItemId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

public class IsNotSupplierPageRule extends AbstractAvailabilityRule {

    private static final Logger logger = LoggerFactory.getLogger(IsNotSupplierPageRule.class);

    @Override
    protected boolean isAvailableForItem(Object itemId) {
        if (itemId instanceof JcrItemId) {
            try {
                return !dependentPagesAvailable((JcrItemId) itemId);
            } catch (RepositoryException e) {
                logger.error("Cannot apply rule", e);
            }
        }
        return true;
    }

    private boolean dependentPagesAvailable(JcrItemId itemId) throws RepositoryException {
        QueryManager queryManager = MgnlContext.getJCRSession(itemId.getWorkspace())
                .getWorkspace().getQueryManager();
        String expression = "SELECT * FROM [" + NodeTypes.Page.NAME + "] WHERE ["
                + NodeObjectMapper.PARENT_PROPERTY + "] = '" + itemId.getUuid() + "'";
        Query query = queryManager.createQuery(expression, Query.JCR_SQL2);
        QueryResult result = query.execute();
        return result.getRows().hasNext();
    }
}

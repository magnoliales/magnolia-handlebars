package com.magnoliales.handlebars.availability;

import info.magnolia.context.MgnlContext;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.api.availability.AbstractAvailabilityRule;
import info.magnolia.ui.vaadin.integration.jcr.JcrItemId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

public class IsNotSupplierPageRule extends AbstractAvailabilityRule {

    private static final Logger log = LoggerFactory.getLogger(IsNotSupplierPageRule.class);

    @Override
    protected boolean isAvailableForItem(Object itemId) {
        if (itemId instanceof JcrItemId) {
            JcrItemId jcrItemId = (JcrItemId) itemId;
            if (jcrItemId.getWorkspace().equals(RepositoryConstants.WEBSITE)) {
                try {
                    return !dependentPagesAvailable(jcrItemId.getUuid());
                } catch (RepositoryException e) {
                    e.printStackTrace();
                    log.error("Cannot apply rule", e);
                }
            }
        }
        return true;
    }

    private boolean dependentPagesAvailable(String nodeId) throws RepositoryException {
        QueryManager queryManager = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE)
                .getWorkspace().getQueryManager();
        String expression = "SELECT * FROM [mgnl:page] WHERE [mgnl:supplierPage] = '" + nodeId + "'";
        Query query = queryManager.createQuery(expression, Query.JCR_SQL2);
        QueryResult result = query.execute();
        return result.getRows().hasNext();
    }
}

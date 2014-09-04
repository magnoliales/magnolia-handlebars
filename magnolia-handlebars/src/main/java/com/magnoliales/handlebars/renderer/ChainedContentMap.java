package com.magnoliales.handlebars.renderer;

import info.magnolia.jcr.util.ContentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

public class ChainedContentMap extends ContentMap {

    private static final Logger log = LoggerFactory.getLogger(ChainedContentMap.class);

    private ChainedContentMap supplierMap;

    public ChainedContentMap(Node node) {
        super(node);
        try {
            if (node.hasProperty("mgnl:supplierPage")) {
                String id = node.getProperty("mgnl:supplierPage").getString();
                supplierMap = new ChainedContentMap(node.getSession().getNodeByIdentifier(id));
            }
        } catch (RepositoryException e) {
            log.error("Cannot fetch supplier page data");
        }
    }

    @Override
    public Object get(Object key) {
        if (key.equals("supplier")) {
            return supplierMap;
        } else if (containsKey(key)) {
            return super.get(key);
        } else {
            return supplierMap.get(key);
        }
    }
}

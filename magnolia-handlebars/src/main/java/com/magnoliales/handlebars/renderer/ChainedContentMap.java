package com.magnoliales.handlebars.renderer;

import info.magnolia.jcr.util.ContentMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.util.Locale;

public class ChainedContentMap extends ContentMap {

    private static final Logger log = LoggerFactory.getLogger(ChainedContentMap.class);

    private ChainedContentMap supplierMap;
    private String localeName;

    public ChainedContentMap(Node node, Locale locale) {
        super(node);
        this.localeName = locale.toString();
        try {
            if (node.hasProperty("mgnl:supplierPage")) {
                String id = node.getProperty("mgnl:supplierPage").getString();
                supplierMap = new ChainedContentMap(node.getSession().getNodeByIdentifier(id), locale);
            }
        } catch (RepositoryException e) {
            log.error("Cannot fetch supplier page data");
        }
    }

    @Override
    public Object get(Object key) {
        String keyName = (String) key;
        String localKeyName = keyName + "_" + localeName;
        if (key.equals("supplier")) {
            return supplierMap;
        } else if (containsKey(localKeyName)) {
            return super.get(localKeyName);
        } else if (containsKey(keyName)) {
            return super.get(keyName);
        } else if (supplierMap != null) {
            return supplierMap.get(key);
        } else {
            return null;
        }
    }
}

package com.magnoliales.handlebars.utils.dam;

import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.utils.PropertyReader;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;

import javax.inject.Inject;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import java.util.Map;

public class AssetReader implements PropertyReader {

    private DamTemplatingFunctions damTemplatingFunction;

    @Inject
    public AssetReader(DamTemplatingFunctions damTemplatingFunction) {
        this.damTemplatingFunction = damTemplatingFunction;
    }

    @Override
    public Asset read(Property property, NodeObjectMapper mapper, Map<String, Object> objectCache)
            throws RepositoryException {
        return damTemplatingFunction.getAsset(property.getString());
    }
}

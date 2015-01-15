package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.objectfactory.Components;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class BeanTemplateHelper implements Helper<Object> {

    private static final Logger log = LoggerFactory.getLogger(BeanTemplateHelper.class);

    private DamTemplatingFunctions damTemplatingFunctions;


    public BeanTemplateHelper() {
        this.damTemplatingFunctions = Components.getComponent(DamTemplatingFunctions.class);
    }

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {
        String properties = "";
        try {
            Map propertiesMap = BeanUtils.describe(context);
            properties = propertiesMap.toString();
        } catch (IllegalAccessException e) {
            properties = e.getMessage();
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            properties = e.getMessage();
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            properties = e.getMessage();
            e.printStackTrace();
        }
        return properties;

    }
}

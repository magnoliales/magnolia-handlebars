package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.objectfactory.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImgTemplateHelper implements Helper<String> {

    private static final Logger log = LoggerFactory.getLogger(ImgTemplateHelper.class);

    private DamTemplatingFunctions damTemplatingFunctions;


    public ImgTemplateHelper() {
        this.damTemplatingFunctions = Components.getComponent(DamTemplatingFunctions.class);
    }

    @Override
    public CharSequence apply(String context, Options options) throws IOException {


        String attributes = "";
        Map<String, String> attributeMap = new HashMap<String,String>();
        Asset asset = this.damTemplatingFunctions.getAsset(context);
        if( asset != null) {
            String src = asset.getLink();
            String alt = asset.getCaption();
            attributeMap.put("src", src);
            attributeMap.put("alt", alt);
        } else {
            log.error("Could not get asset with itemKey " + context );
        }

        for (Map.Entry<String, Object> entry : options.hash.entrySet())
        {
            if (entry.getValue() != null) {
                attributeMap.put(entry.getKey(), entry.getValue().toString());
            }
        }

        for (Map.Entry<String, String> entry : attributeMap.entrySet())
        {
            if (entry.getValue() != null) {
                attributes += String.format("%s=\"%s\" ", entry.getKey(), entry.getValue());
            }
        }

        return String.format("<img %s/>", attributes);

    }
}

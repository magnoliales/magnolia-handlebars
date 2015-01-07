package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import info.magnolia.dam.api.Asset;
import info.magnolia.dam.templating.functions.DamTemplatingFunctions;
import info.magnolia.objectfactory.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;

public class ImgTemplateHelper implements Helper<String> {

    private static final Logger log = LoggerFactory.getLogger(ImgTemplateHelper.class);

    private DamTemplatingFunctions damTemplatingFunctions;


    public ImgTemplateHelper() {
        this.damTemplatingFunctions = Components.getComponent(DamTemplatingFunctions.class);
    }

    @Override
    public CharSequence apply(String context, Options options) throws IOException {

        String src = "";
        String id = options.hash("id");
        String alt = "";
        String className = options.hash("class");
        Asset asset = this.damTemplatingFunctions.getAsset(context);
        if( asset != null) {
            src = asset.getLink();
            alt = asset.getCaption();
        } else {
            log.error("Could not get asset with itemKey " + id );
        }

        return String.format("<img src=\"%s\" id=\"%s\" class=\"%s\"/ alt=\"%s\">", src, id, className, alt);

    }
}

package com.magnoliales.handlebars.rendering.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import info.magnolia.link.LinkUtil;
import javax.jcr.RepositoryException;
import java.io.IOException;

public class DamLinkTemplateHelper implements Helper<String> {

    @Override
    public CharSequence apply(String context, Options options) throws IOException {
        String id = context.replaceFirst("jcr:", "");
        try {
            return LinkUtil.createLink("dam", id);
        } catch (RepositoryException e) {
            return null;
        }
    }
}

package com.magnoliales.handlebars.templating.helpers;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class CmsComponentTemplateHelper implements Helper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HandlebarsInitTemplateHelper.class);

    public CharSequence apply(Object context, Options options) throws IOException {

        /*
        Node node = ((ContentMap) context).getJCRNode();

        String workspace = null;
        String nodeIdentifier = null;
        String path = null;
        try {
            workspace = node.getSession().getWorkspace().getName();
            nodeIdentifier = node.getIdentifier();
            path = node.getPath();
        } catch (RepositoryException e) {
            LOGGER.error("Cannot read properties from the node", e);
        }

        final ComponentElement templatingElement = createTemplatingElement();

        templatingElement.setContent(node);
        templatingElement.setWorkspace(workspace);
        templatingElement.setNodeIdentifier(nodeIdentifier);
        templatingElement.setPath(path);

        String dialog = options.hash("dialog");
        Boolean editable = options.hash("editable");
        Map<String, Object> contextAttributes = options.hash("contextAttributes");

        templatingElement.setDialog(dialog);
        templatingElement.setContextAttributes(contextAttributes);
        templatingElement.setEditable(editable);

        return render(templatingElement);
        */
        return null;
    }
}

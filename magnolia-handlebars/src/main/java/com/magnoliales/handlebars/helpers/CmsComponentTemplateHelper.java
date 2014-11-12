package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import info.magnolia.jcr.util.ContentMap;
import info.magnolia.templating.elements.ComponentElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

public class CmsComponentTemplateHelper extends AbstractTemplateHelper<ComponentElement> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsInitTemplateHelper.class);

    public CharSequence apply(Object context, Options options) throws IOException {

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
    }
}

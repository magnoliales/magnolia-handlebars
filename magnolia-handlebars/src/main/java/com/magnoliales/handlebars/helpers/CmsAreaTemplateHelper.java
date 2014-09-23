package com.magnoliales.handlebars.helpers;

import com.github.jknack.handlebars.Options;
import info.magnolia.rendering.model.RenderingModel;
import info.magnolia.templating.elements.AreaElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import java.io.IOException;
import java.util.Map;

public class CmsAreaTemplateHelper extends AbstractTemplateHelper<AreaElement> {

    private static final Logger log = LoggerFactory.getLogger(CmsInitTemplateHelper.class);

    public CharSequence apply(Object context, Options options) throws IOException {

        String name = options.hash("name");
        String availableComponents = options.hash("components");
        String dialog = options.hash("dialog");
        String type = options.hash("type");
        String label = options.hash("label");
        String description = options.hash("description");
        Boolean editable = options.hash("editable");
        Map<String, Object> contextAttributes = options.hash("contextAttributes");

        RenderingModel model = (RenderingModel) ((Map) context).get("model");
        AreaState areaState = createAreaState(name, model.getNode());
        Node node = areaState.getNode();

        String workspace = null;
        String nodeIdentifier = null;
        String path = null;
        try {
            workspace = node.getSession().getWorkspace().getName();
            nodeIdentifier = node.getIdentifier();
            path = node.getPath();
        } catch (RepositoryException e) {
            log.error("Cannot read properties from the node", e);
        }

        final AreaElement templatingElement = createTemplatingElement();

        templatingElement.setContent(node);
        templatingElement.setWorkspace(workspace);
        templatingElement.setNodeIdentifier(nodeIdentifier);
        templatingElement.setPath(path);
        templatingElement.setArea(areaState.getAreaDefinition());

        templatingElement.setName(name);
        templatingElement.setAvailableComponents(availableComponents);
        templatingElement.setDialog(dialog);
        templatingElement.setType(type);
        templatingElement.setLabel(label);
        templatingElement.setDescription(description);
        templatingElement.setEditable(editable);
        templatingElement.setContextAttributes(contextAttributes);

        return render(templatingElement);
    }

}

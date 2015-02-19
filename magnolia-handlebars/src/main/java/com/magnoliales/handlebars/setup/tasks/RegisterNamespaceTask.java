package com.magnoliales.handlebars.setup.tasks;

import info.magnolia.context.MgnlContext;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.AbstractTask;
import info.magnolia.module.delta.TaskExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.NamespaceRegistry;
import javax.jcr.RepositoryException;

public class RegisterNamespaceTask extends AbstractTask {

    private static final Logger logger = LoggerFactory.getLogger(RegisterNamespaceTask.class);

    private final String workspace;
    private final String prefix;
    private final String uri;

    public RegisterNamespaceTask(String name, String workspace, String prefix, String uri) {
        super(name, String.format("Adding namespace with prefix '%s' and URI '%s'", prefix, uri));
        this.workspace = workspace;
        this.prefix = prefix;
        this.uri = uri;
    }

    @Override
    public void execute(InstallContext installContext) throws TaskExecutionException {
        try {
            NamespaceRegistry namespaceRegistry =
                    MgnlContext.getJCRSession(workspace).getWorkspace().getNamespaceRegistry();
            namespaceRegistry.registerNamespace(prefix, uri);
            logger.info("Registered namespace {} {}", prefix, uri);
        } catch (RepositoryException e) {
            logger.error("Cannot register namespace {} {}", prefix, uri);
            throw new TaskExecutionException(e.getMessage(), e);
        }
    }
}

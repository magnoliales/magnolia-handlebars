package com.magnoliales.handlebars.setup;

import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;
import info.magnolia.module.blossom.module.BlossomModuleSupport;
import info.magnolia.objectfactory.Components;
import org.springframework.web.servlet.DispatcherServlet;

public class HandlebarsModule extends BlossomModuleSupport implements ModuleLifecycle {

    public static final String SERVLET_NAME = "handlebars-blossom";

    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        initBlossomDispatcherServlet(SERVLET_NAME, "classpath:/handlebars-context.xml");
        for (DispatcherServlet servlet : getDispatcherServlets()) {
            if (servlet.getServletName().equals(SERVLET_NAME)) {
                ApplicationContextAdapter container = Components.getComponent(ApplicationContextAdapter.class);
                container.setContext(servlet.getWebApplicationContext());
            }
        }
    }

    public void stop(ModuleLifecycleContext moduleLifecycleContext) {
        super.destroyDispatcherServlets();
        super.closeRootWebApplicationContext();
    }
}
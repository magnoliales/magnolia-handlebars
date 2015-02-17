package com.magnoliales.handlebars.example.setup;

import com.magnoliales.handlebars.setup.registry.HandlebarsRegistry;
import info.magnolia.module.ModuleLifecycle;
import info.magnolia.module.ModuleLifecycleContext;

import javax.inject.Inject;

public class HandlebarsExampleModule implements ModuleLifecycle {

    private final HandlebarsRegistry handlebarsRegistry;

    @Inject
    public HandlebarsExampleModule(HandlebarsRegistry handlebarsRegistry) {
        this.handlebarsRegistry = handlebarsRegistry;
    }

    public void start(ModuleLifecycleContext moduleLifecycleContext) {
        handlebarsRegistry.init("com.magnoliales.handlebars.example.templates");
    }

    public void stop(ModuleLifecycleContext moduleLifecycleContext) { }
}

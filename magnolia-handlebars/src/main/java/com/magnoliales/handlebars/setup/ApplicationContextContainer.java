package com.magnoliales.handlebars.setup;

import org.springframework.context.ApplicationContext;

public class ApplicationContextContainer {

    private ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public ApplicationContext getContext() {
        return context;
    }
}

package com.magnoliales.handlebars.setup;

import com.magnoliales.handlebars.annotations.ParentTemplate;
import com.magnoliales.handlebars.annotations.SingletonTemplate;
import info.magnolia.module.blossom.annotation.Template;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContextAdapter {

    private ApplicationContext context;

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public Map<String, TemplateOption> getTemplateOptions() {
        Map<String, TemplateOption> options = new LinkedHashMap<String, TemplateOption>();
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            Class<?> beanClass = context.getBean(beanDefinitionName).getClass();
            if (beanClass.isAnnotationPresent(Template.class)) {
                Template template = beanClass.getAnnotation(Template.class);
                String id = template.id();
                String title = template.title();
                boolean isSingleton = beanClass.isAnnotationPresent(SingletonTemplate.class);
                TemplateOption option;
                if (beanClass.isAnnotationPresent(ParentTemplate.class)) {
                    String parentId = beanClass.getAnnotation(ParentTemplate.class).value()
                            .getAnnotation(Template.class).id();
                    option = new TemplateOption(id, title, isSingleton, parentId);
                } else {
                    option = new TemplateOption(id, title, isSingleton);
                }
                options.put(option.getId(), option);
            }
        }
        return options;
    }

    public static class TemplateOption {

        final private String id;
        final private String title;
        final private String parentId;
        final private boolean isSingleton;

        public TemplateOption(String id, String title, boolean isSingleton) {
            this(id, title, isSingleton, null);
        }

        public TemplateOption(String id, String title, boolean isSingleton, String parentId) {
            this.id = id;
            this.title = title;
            this.isSingleton = isSingleton;
            this.parentId = parentId;
        }

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getParentId() {
            return parentId;
        }

        public boolean isSingleton() {
            return isSingleton;
        }

        public boolean requiresParent() {
            return parentId != null;
        }
    }
}

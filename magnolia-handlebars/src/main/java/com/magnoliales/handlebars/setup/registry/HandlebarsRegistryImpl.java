package com.magnoliales.handlebars.setup.registry;

import com.magnoliales.handlebars.annotations.Area;
import com.magnoliales.handlebars.annotations.Component;
import com.magnoliales.handlebars.annotations.Page;
import com.magnoliales.handlebars.mapper.NodeObjectMapper;
import com.magnoliales.handlebars.rendering.definition.HandlebarsAreaDefinition;
import com.magnoliales.handlebars.rendering.definition.HandlebarsComponentDefinition;
import com.magnoliales.handlebars.rendering.definition.HandlebarsPageDefinition;
import com.magnoliales.handlebars.rendering.definition.HandlebarsTemplateDefinitionProvider;
import com.magnoliales.handlebars.ui.dialogs.AnnotatedDialogDefinitionFactory;
import info.magnolia.context.MgnlContext;
import info.magnolia.i18nsystem.SimpleTranslator;
import info.magnolia.jcr.util.NodeTypes;
import info.magnolia.rendering.template.AreaDefinition;
import info.magnolia.rendering.template.TemplateDefinition;
import info.magnolia.rendering.template.registry.TemplateDefinitionRegistry;
import info.magnolia.repository.RepositoryConstants;
import info.magnolia.ui.dialog.registry.DialogDefinitionProvider;
import info.magnolia.ui.dialog.registry.DialogDefinitionRegistry;
import org.apache.jackrabbit.commons.JcrUtils;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;

public class HandlebarsRegistryImpl implements HandlebarsRegistry {

    private static Logger logger = LoggerFactory.getLogger(HandlebarsRegistryImpl.class);

    private boolean initialized;
    private AnnotatedDialogDefinitionFactory annotatedDialogDefinitionFactory;
    private Map<Class<?>, TemplateDefinition> templateDefinitions;
    private TemplateDefinitionRegistry templateDefinitionRegistry;
    private DialogDefinitionRegistry dialogDefinitionRegistry;
    private SimpleTranslator translator;
    private Reflections reflections;

    @Inject
    public HandlebarsRegistryImpl(AnnotatedDialogDefinitionFactory annotatedDialogDefinitionFactory,
                                  TemplateDefinitionRegistry templateDefinitionRegistry,
                                  DialogDefinitionRegistry dialogDefinitionRegistry,
                                  SimpleTranslator translator) {
        this.annotatedDialogDefinitionFactory = annotatedDialogDefinitionFactory;
        this.templateDefinitionRegistry = templateDefinitionRegistry;
        this.dialogDefinitionRegistry = dialogDefinitionRegistry;
        this.translator = translator;
    }

    @Override
    public void init(String... namespaces) {
        if (initialized) {
            throw new IllegalStateException("Handlebars registry is already initialized");
        }
        Set<Class<?>> pageClasses = new HashSet<>();
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        for (String namespace : namespaces) {
            configurationBuilder.addUrls(ClasspathHelper.forPackage(namespace));
        }
        reflections = new Reflections(configurationBuilder);
        pageClasses.addAll(reflections.getTypesAnnotatedWith(Page.class, true));
        processDefinitions(pageClasses, new HashMap<Class<?>, TemplateDefinition>());
        initialized = true;
    }

    @Override
    public List<TemplateDefinition> getTemplateDefinitions() {
        if (!initialized) {
            throw new IllegalStateException("Handlebars registry is not initialized");
        }
        List<TemplateDefinition> definitionsList = new ArrayList<>(templateDefinitions.values());
        Collections.sort(definitionsList, new Comparator<TemplateDefinition>() {
            @Override
            public int compare(TemplateDefinition o1, TemplateDefinition o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        return definitionsList;
    }

    private void processDefinitions(Set<Class<?>> unprocessedClasses,
                                    Map<Class<?>, TemplateDefinition> processedDefinitions) {
        if (unprocessedClasses.isEmpty()) {
            this.templateDefinitions = processedDefinitions;
            return;
        }
        for (Class<?> pageClass : unprocessedClasses) {
            Class<?> pageSuperclass = getPageSuperclass(pageClass);
            if (pageSuperclass != null && !processedDefinitions.containsKey(pageSuperclass)) {
                continue;
            }
            Map<String, AreaDefinition> areas = new HashMap<>();
            discoverAreas(pageClass, areas, processedDefinitions);
            HandlebarsPageDefinition pageDefinition;
            if (pageSuperclass != null) {
                HandlebarsPageDefinition parent = (HandlebarsPageDefinition) processedDefinitions.get(pageSuperclass);
                pageDefinition = new HandlebarsPageDefinition(pageClass, translator, areas, parent);
            } else {
                pageDefinition = new HandlebarsPageDefinition(pageClass, translator, areas);
            }

            for (DialogDefinitionProvider provider : annotatedDialogDefinitionFactory.discoverDialogProviders(pageClass)) {
                dialogDefinitionRegistry.register(provider);
            }

            templateDefinitionRegistry.register(new HandlebarsTemplateDefinitionProvider(pageDefinition));
            processedDefinitions.put(pageClass, pageDefinition);
        }
        unprocessedClasses.removeAll(processedDefinitions.keySet());
        processDefinitions(unprocessedClasses, processedDefinitions);
    }

    private void discoverAreas(Class<?> currentContainerClass,
                               Map<String, AreaDefinition> areas,
                               Map<Class<?>, TemplateDefinition> processedDefinitions) {
        for (Field pageField : currentContainerClass.getDeclaredFields()) {
            if (pageField.getType().isAnnotationPresent(Area.class)) {
                List<Class<?>> components = new ArrayList<>();
                for (Field areaField : pageField.getType().getDeclaredFields()) {
                    if (areaField.getType().isArray()) {
                        Class<?> componentClass = areaField.getType().getComponentType();
                        if (componentClass.isInterface()){
                            for(Class<?> clazz: reflections.getSubTypesOf(componentClass)) {
                                if (clazz.isAnnotationPresent(Component.class)) {
                                    components.add(clazz);
                                    HandlebarsComponentDefinition componentDefinition = new HandlebarsComponentDefinition(clazz, translator);
                                    templateDefinitionRegistry.register(new HandlebarsTemplateDefinitionProvider(componentDefinition));
                                    processedDefinitions.put(clazz, componentDefinition);
                                }
                            }
                        } else if (componentClass.isAnnotationPresent(Component.class)) {
                            components.add(componentClass);
                            HandlebarsComponentDefinition componentDefinition = new HandlebarsComponentDefinition(componentClass, translator);
                            templateDefinitionRegistry.register(new HandlebarsTemplateDefinitionProvider(componentDefinition));
                            processedDefinitions.put(componentClass, componentDefinition);
                        }
                    }
                }
                HandlebarsAreaDefinition areaDefinition = new HandlebarsAreaDefinition(pageField.getType(), translator, components);
                HandlebarsTemplateDefinitionProvider areaDefinitionProvider = new HandlebarsTemplateDefinitionProvider(areaDefinition);
                templateDefinitionRegistry.register(areaDefinitionProvider);
                areas.put(pageField.getName(), areaDefinition);
            }
        }
    }

    private Class<?> getPageSuperclass(Class<?> pageClass) {
        if (pageClass == Object.class) {
            return null;
        }
        Class<?> superclass = pageClass.getSuperclass();
        if (superclass.isAnnotationPresent(Page.class)) {
            return superclass;
        } else {
            return getPageSuperclass(superclass);
        }
    }

    @Override
    public boolean pageWithTemplateExists(String template) {
        return getPagesByTemplate(template).size() > 0;
    }

    @Override
    public Map<String, String> getPagesByTemplate(String template) {
        Map<String, String> pages = new TreeMap<>();
        String expression = "SELECT * FROM [" + NodeTypes.Page.NAME
                + "] WHERE [" + NodeObjectMapper.CLASS_PROPERTY + "] = '" + template + "'";
        try {
            Session session = MgnlContext.getJCRSession(RepositoryConstants.WEBSITE);
            QueryManager queryManager = session.getWorkspace().getQueryManager();
            Query query = queryManager.createQuery(expression, Query.JCR_SQL2);
            QueryResult result = query.execute();
            for (Row row : JcrUtils.getRows(result)) {
                javax.jcr.Node node = row.getNode();
                pages.put(node.getPath(), node.getIdentifier());
            }
        } catch (RepositoryException e) {
            throw new RuntimeException("Cannot fetch pages using template '" + template + "'", e);
        }
        return pages;
    }

    @Override
    public TemplateDefinition getTemplateDefinition(String template) {
        if (!initialized) {
            throw new IllegalStateException("Handlebars registry is not initialized");
        }
        try {
            Class<?> pageClass = Class.forName(template);
            if (templateDefinitions.containsKey(pageClass)) {
                return templateDefinitions.get(pageClass);
            } else {
                throw new RuntimeException("Cannot find templateScript definitions for class '" + pageClass + "'");
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TemplateDefinition getTemplateDefinition(Node node) {
        try {
            String template = node.getProperty(NodeObjectMapper.CLASS_PROPERTY).getString();
            return getTemplateDefinition(template);
        } catch (RepositoryException e) {
            throw new RuntimeException("Cannot read templateScript name of node '" + node + "'", e);
        }
    }
}

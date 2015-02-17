package com.magnoliales.handlebars.setup;

import com.magnoliales.handlebars.setup.tasks.RegisterNamespaceTask;
import com.magnoliales.handlebars.ui.columns.TemplateColumnFormatter;
import com.magnoliales.handlebars.ui.fields.template.field.PageClassFieldDefinition;
import info.magnolia.module.DefaultModuleVersionHandler;
import info.magnolia.module.InstallContext;
import info.magnolia.module.delta.SetPropertyTask;
import info.magnolia.module.delta.Task;
import info.magnolia.repository.RepositoryConstants;

import java.util.ArrayList;
import java.util.List;

public class HandlebarsVersionHandler extends DefaultModuleVersionHandler {

    @Override
    protected List<Task> getExtraInstallTasks(InstallContext installContext) {
        List<Task> tasks = new ArrayList<>();

        tasks.add(new RegisterNamespaceTask("Adding handlebars namespace",
                RepositoryConstants.WEBSITE, "handlebars", "http://magnoliales.com/handlebars"));

        tasks.add(new SetPropertyTask("Replace templateScript selector in pages:createPage dialog",
                RepositoryConstants.CONFIG,
                "/modules/pages/dialogs/createPage/form/tabs/tabPage/fields/template",
                "class", PageClassFieldDefinition.class.getName()));
        tasks.add(new SetPropertyTask("Replace templateScript selector in pages:editTemplate dialog",
                RepositoryConstants.CONFIG,
                "/modules/pages/dialogs/editTemplate/form/tabs/tabTemplate/fields/template",
                "class", PageClassFieldDefinition.class.getName()));
        tasks.add(new SetPropertyTask("Translate templateScript column formatter",
                RepositoryConstants.CONFIG,
                "/modules/pages/apps/pages/subApps/browser/workbench/contentViews/list/columns/template",
                "formatterClass", TemplateColumnFormatter.class.getName()));

        return tasks;
    }
}

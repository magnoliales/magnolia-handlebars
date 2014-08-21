package com.magnoliales.handlebars.setup;

import com.magnoliales.handlebars.fields.TemplateSelectorDefinition;
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
        List<Task> tasks = new ArrayList<Task>();
        tasks.add(new SetPropertyTask("Replace template selector in pages:createPage dialog",
                RepositoryConstants.CONFIG, "/modules/pages/dialogs/createPage/form/tabs/tabPage/fields/template",
                "class", TemplateSelectorDefinition.class.getName()));
        tasks.add(new SetPropertyTask("Replace template selector in pages:editTemplate dialog",
                RepositoryConstants.CONFIG, "/modules/pages/dialogs/editTemplate/form/tabs/tabTemplate/fields/template",
                "class", TemplateSelectorDefinition.class.getName()));
        tasks.add(new SetPropertyTask("Disable default subscription",
                RepositoryConstants.CONFIG, "server/activation/subscribers/magnoliaPublic8080",
                "active", Boolean.FALSE));

        return tasks;
    }
}

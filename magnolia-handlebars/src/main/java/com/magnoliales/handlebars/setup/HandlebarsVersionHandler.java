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
        tasks.add(new SetPropertyTask("Add template pair selector", RepositoryConstants.CONFIG,
                "/modules/pages/dialogs/createPage/form/tabs/tabPage/fields/template", "class",
                TemplateSelectorDefinition.class.getName()));
        return tasks;
    }
}

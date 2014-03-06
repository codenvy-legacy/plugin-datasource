package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.resources.model.File;
import com.codenvy.ide.resources.model.Folder;
import com.codenvy.ide.resources.model.Project;
import com.codenvy.ide.resources.model.Resource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class SqlResourceProvider extends NewResourceProvider {

    private static final String NEW_SQL_PROVIDER_ID = "new_sql";

    @Inject
    public SqlResourceProvider(final SqlEditorConstants constants,
                               final SqlEditorResources resources,
                               final IconRegistry iconRegistry) {
        super(NEW_SQL_PROVIDER_ID,
              constants.newSqlWizardTitle(),
              iconRegistry.getIcon("default.sqlfile.icon"),
              SqlEditorExtension.SQL_FILE_EXTENSION);
    }

    @Override
    public void create(final String name, final Folder parent,
                       final Project project, final AsyncCallback<Resource> callback) {
        final String filename = name + "." + getExtension();
        project.createFile(parent, filename, "", SqlEditorExtension.GENERIC_SQL_MIME_TYPE, new AsyncCallback<File>() {

            @Override
            public void onFailure(final Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(final File result) {
                callback.onSuccess(result);
            }
        });
    }

}

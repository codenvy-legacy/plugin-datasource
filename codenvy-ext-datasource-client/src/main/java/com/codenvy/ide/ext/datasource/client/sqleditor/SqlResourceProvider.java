/*
 * Copyright 2014 Codenvy, S.A.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codenvy.ide.ext.datasource.client.sqleditor;

import com.codenvy.ide.api.resources.model.File;
import com.codenvy.ide.api.resources.model.Folder;
import com.codenvy.ide.api.resources.model.Project;
import com.codenvy.ide.api.resources.model.Resource;
import com.codenvy.ide.api.ui.IconRegistry;
import com.codenvy.ide.api.ui.wizard.newresource.NewResourceProvider;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
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
              iconRegistry.getIcon("default.sqlfile.icon").getImage(),
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
